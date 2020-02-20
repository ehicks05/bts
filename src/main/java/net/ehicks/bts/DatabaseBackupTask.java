package net.ehicks.bts;

import it.sauronsoftware.cron4j.Scheduler;
import net.ehicks.bts.beans.BtsSystem;
import net.ehicks.bts.beans.BtsSystemRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DatabaseBackupTask
{
    private static final Logger log = LoggerFactory.getLogger(DatabaseBackupTask.class);
    private static final Scheduler scheduler = new Scheduler();
    private static boolean running = false;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${puffin.pgDumpPath}")
    private String pgDumpPath;

    private BtsSystemRepository btsSystemRepository;

    public DatabaseBackupTask(BtsSystemRepository btsSystemRepository)
    {
        this.btsSystemRepository = btsSystemRepository;
    }

    public static Scheduler getScheduler()
    {
        return scheduler;
    }

    public Path getBackupPath()
    {
        BtsSystem btsSystem = btsSystemRepository.findFirstBy();
        String filename = "bts_" + LocalDate.now().toString() + ".dump";
        return Paths.get(btsSystem.getBackupDir(), filename);
    }

    public void scheduleTask()
    {
        String taskId = scheduler.schedule("0 2 * * *", this::backupToZip);
        log.info("Scheduling task " + taskId);
        scheduler.start();
    }

    public void backupToZip()
    {
        if (running)
            return;
        
        new Thread(() -> {
            try
            {
                running = true;

                log.info("Starting BackupDbTask");
                Path backupPath = getBackupPath();
                Files.createDirectories(backupPath.getParent());

                String backupFilename = backupPath.getFileName().toString();
                String zippedFilename = backupFilename.replace(".dump", ".zip");
                Path zippedBackupPath = backupPath.subpath(0, backupPath.getNameCount() - 1).resolve(zippedFilename);
                Files.deleteIfExists(zippedBackupPath);

                doPgDump(backupPath);

                log.info("Starting BackupDbTask zip");
                zipDumpFile(backupPath, zippedBackupPath);
                log.info("Finished BackupDbTask zip");

                Files.deleteIfExists(backupPath);
                log.info("Finished BackupDbTask");
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            finally
            {
                running = false;
            }
        }).start();
    }

    private void zipDumpFile(Path backupPath, Path zippedBackupPath) throws IOException
    {
        try (InputStream in = new BufferedInputStream(new FileInputStream(backupPath.toFile()));
             ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedBackupPath.toFile())))
        {
            ZipEntry zipEntry = new ZipEntry(backupPath.getFileName().toString());
            out.putNextEntry(zipEntry);

            IOUtils.copy(in, out);
        }
    }

    public void doPgDump(Path backupPath)
    {
        String hostThroughDbName = url.substring(url.indexOf("://") + 3, url.indexOf("?"));
        String urlString = "postgres://" + username + ":" + password + "@" + hostThroughDbName;

        ProcessBuilder builder = new ProcessBuilder(
                pgDumpPath,
                "--dbname=" + urlString,
                "--file=" + backupPath.toString(),
                "--format=custom",
//                "--verbose",
                "--no-password");

        builder.redirectErrorStream(true);
        Map<String, String> env = builder.environment();
        env.put("PGPASSWORD", password);

        try
        {
            Process process = builder.start();
            OutputStream processIn = process.getOutputStream();
            InputStream processOut = process.getInputStream();

            new Thread(() -> {
                try
                {
                    byte[] bytes = new byte[4096];
                    while (processOut.read(bytes) != -1)
                    {
                        log.info(new String(bytes, StandardCharsets.UTF_8));
                    }
                }
                catch (IOException e)
                {
                    log.error(e.getMessage(), e);
                }
            }).start();

            process.waitFor();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    public static boolean isRunning()
    {
        return running;
    }
}
