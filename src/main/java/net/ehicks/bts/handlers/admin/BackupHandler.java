package net.ehicks.bts.handlers.admin;

import net.ehicks.bts.DatabaseBackupTask;
import net.ehicks.bts.beans.BtsSystemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class BackupHandler
{
    private static final Logger log = LoggerFactory.getLogger(BackupHandler.class);

    private BtsSystemRepository btsSystemRepository;
    private DatabaseBackupTask databaseBackupTask;

    public BackupHandler(BtsSystemRepository btsSystemRepository, DatabaseBackupTask databaseBackupTask)
    {
        this.btsSystemRepository = btsSystemRepository;
        this.databaseBackupTask = databaseBackupTask;
    }

    @GetMapping("/admin/backups/form")
    public ModelAndView showBackups()
    {
        File backupDir = new File(btsSystemRepository.findFirstBy().getBackupDir());
        List<File> backups = new ArrayList<>();
        if (backupDir.exists() && backupDir.isDirectory())
            backups = new ArrayList<>(Arrays.asList(backupDir.listFiles()));
        backups.removeIf(file -> !file.isFile() && !file.getName().contains("bts"));
        Collections.reverse(backups);

        return new ModelAndView("admin/backups")
                .addObject("backupPath", Paths.get(btsSystemRepository.findFirstBy().getBackupDir()))
                .addObject("backups", backups)
                .addObject("isRunning", DatabaseBackupTask.isRunning());
    }

    @GetMapping("/admin/backups/create")
    public ModelAndView createBackup()
    {
        databaseBackupTask.backupToZip();

        return new ModelAndView("redirect:/admin/backups/form");
    }

    @GetMapping("/admin/backups/checkStatus")
    @ResponseBody
    public boolean checkStatus()
    {
        return DatabaseBackupTask.isRunning();
    }

    @GetMapping("/admin/backups/delete")
    public ModelAndView deleteBackup(@RequestParam String backupName)
    {
        Path backupPath = Paths.get(btsSystemRepository.findFirstBy().getBackupDir(), backupName);
        boolean result = backupPath.toFile().delete();
        return new ModelAndView("redirect:/admin/backups/form");
    }

    @GetMapping("/admin/backups/viewBackup")
    @ResponseBody
    public ResponseEntity<byte[]> viewBackup(@RequestParam String backupName)
    {
        try
        {
            Path backupPath = Paths.get(btsSystemRepository.findFirstBy().getBackupDir(), backupName);
            byte[] output = new BufferedInputStream(new FileInputStream(backupPath.toFile())).readAllBytes();

            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(backupPath.getFileName().toString()).build();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentDisposition(contentDisposition);
            
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(output);
        }
        catch (Exception e)
        {
            log.error(e.getLocalizedMessage());
        }
        return ResponseEntity.unprocessableEntity().build();
    }
}
