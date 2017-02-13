package net.ehicks.bts;

import net.ehicks.eoi.EOIBackup;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupDbTask
{
    private static final Logger log = LoggerFactory.getLogger(BackupDbTask.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }

    public static String getBackupPath() throws IOException
    {
        String today = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        String filename = "bts_" + today + ".sql";
        File backupFile = new File(SystemInfo.INSTANCE.getBackupDirectory() + filename);
        return backupFile.getCanonicalPath();
    }

    public static void scheduleTask()
    {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.systemDefault();
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNext2Hours;
        zonedNext2Hours = zonedNow.withHour(2).withMinute(0).withSecond(0);
        if(zonedNow.compareTo(zonedNext2Hours) > 0)
            zonedNext2Hours = zonedNext2Hours.plusDays(1);

        Duration duration = Duration.between(zonedNow, zonedNext2Hours);
        long initialDelay = duration.getSeconds();

        scheduler.scheduleAtFixedRate(BackupDbTask::backupToZip, initialDelay, 24*60*60, TimeUnit.SECONDS);
    }

    public static void backupToZip()
    {
        try
        {
            log.info("Starting BackupDbTask");
            String backupPath = getBackupPath();
            EOIBackup.backup(backupPath);

            try (InputStream inputStream = new BufferedInputStream(new FileInputStream(backupPath));
                 ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(backupPath.replace(".sql", ".zip")));)
            {
                ZipEntry zipEntry = new ZipEntry(new File(backupPath).getName());
                outputStream.putNextEntry(zipEntry);

                IOUtils.copy(inputStream, outputStream);
            }

            new File(backupPath).delete();
            log.info("Finished BackupDbTask");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }
}
