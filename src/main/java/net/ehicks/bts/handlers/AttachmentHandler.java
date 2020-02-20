package net.ehicks.bts.handlers;

import kotlin.Pair;
import net.ehicks.bts.beans.Attachment;
import net.ehicks.bts.beans.AttachmentRepository;
import net.ehicks.bts.beans.DBFile;
import net.ehicks.bts.beans.IssueRepository;
import net.ehicks.bts.model.DBFileLogic;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
public class AttachmentHandler
{
    private DBFileLogic dbFileLogic;
    private AttachmentRepository attachmentRepository;
    private IssueRepository issueRepository;

    public AttachmentHandler(DBFileLogic dbFileLogic, AttachmentRepository attachmentRepository,
                             IssueRepository issueRepository)
    {
        this.dbFileLogic = dbFileLogic;
        this.attachmentRepository = attachmentRepository;
        this.issueRepository = issueRepository;
    }

    @GetMapping("/attachment/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getAttachment(@PathVariable Long id)
    {
        // todo security - something about group access?
        return attachmentRepository.findById(id).map(value -> ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(value.getDbFile().getMediaType()))
                .body(value.getDbFile().getContent()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/issue/addAttachment")
    public ModelAndView addAttachment(@RequestParam Long issueId, @RequestParam("fldFile") MultipartFile file)
    {
        Pair<DBFile, Exception> saveResult = dbFileLogic.saveDBFile(file);
        DBFile dbFile = saveResult.getFirst();
        Exception saveException = saveResult.getSecond();

        if (dbFile != null && saveException == null)
        {
            String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
            issueRepository.findById(issueId).ifPresent(issue ->
                    attachmentRepository.save(new Attachment(0, filename, issue, dbFile, LocalDateTime.now()))
            );

            return new ModelAndView("redirect:/issue/form?issueId=" + issueId)
                    .addObject("responseMessage", "Attachment added.");
        }

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId)
                .addObject("responseMessage", "There was an error uploading attachment.");
    }

    @GetMapping("/issue/deleteAttachment")
    public ModelAndView deleteAttachment(@RequestParam Long issueId, @RequestParam Long attachmentId)
    {
        attachmentRepository.findById(attachmentId).ifPresent(attachment -> {
            // todo security: who has access
//            if (!Security.hasAccess(userSession, attachment.getIssue().getGroup()))
//                return;

            attachmentRepository.delete(attachment);
            dbFileLogic.deleteDBFile(attachment.getDbFile().getId());
        });

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }
}
