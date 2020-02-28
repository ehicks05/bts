package net.ehicks.bts.handlers;

import kotlin.Pair;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.mail.MailClient;
import net.ehicks.bts.model.DBFileLogic;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class AttachmentHandler
{
    private DBFileLogic dbFileLogic;
    private AttachmentRepository attachmentRepository;
    private IssueRepository issueRepository;
    private IssueEventRepository issueEventRepository;
    private MailClient mailClient;

    public AttachmentHandler(DBFileLogic dbFileLogic, AttachmentRepository attachmentRepository,
                             IssueRepository issueRepository, IssueEventRepository issueEventRepository,
                             MailClient mailClient)
    {
        this.dbFileLogic = dbFileLogic;
        this.attachmentRepository = attachmentRepository;
        this.issueRepository = issueRepository;
        this.issueEventRepository = issueEventRepository;
        this.mailClient = mailClient;
    }

    @GetMapping("/attachment/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getAttachment(@PathVariable Long id, @RequestParam(required = false) Boolean thumbnail)
    {
        // todo security - something about group access?

        Attachment attachment = attachmentRepository.findById(id).orElse(null);
        if (attachment != null)
        {
            DBFile dbFile = attachment.getDbFile();
            MediaType mediaType = MediaType.parseMediaType(dbFile.getMediaType());

            byte[] content = null;
            if (thumbnail != null && thumbnail && dbFile.getThumbnail() != null)
                content = dbFile.getThumbnail().getContent();
            if (thumbnail == null || !thumbnail)
                content = dbFile.getContent();

            if (content != null)
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(content);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/issue/addAttachment")
    public ModelAndView addAttachment(RedirectAttributes redirectAttrs, @AuthenticationPrincipal User user,
                                      @RequestParam Long issueId, @RequestParam("fldFile") MultipartFile file)
    {
        Pair<DBFile, Exception> saveResult = dbFileLogic.saveDBFile(file);
        DBFile dbFile = saveResult.getFirst();
        Exception saveException = saveResult.getSecond();

        if (dbFile != null && saveException == null)
        {
            String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
            issueRepository.findById(issueId).ifPresent(issue -> {
                attachmentRepository.save(new Attachment(0, filename, issue, dbFile, LocalDateTime.now(), user));
                IssueEvent issueEvent = issueEventRepository.save(new IssueEvent(0, user, issue, EventType.ADD, "attachment", "", filename));
                mailClient.prepareAndSend(issueEvent);
            });

            redirectAttrs.addFlashAttribute("responseMessage", "Attachment added.");
            return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
        }

        redirectAttrs.addFlashAttribute("responseMessage", "There was an error uploading attachment.");
        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }

    @GetMapping("/issue/deleteAttachment")
    public ModelAndView deleteAttachment(RedirectAttributes redirectAttrs, @AuthenticationPrincipal User user,
                                         @RequestParam Long issueId, @RequestParam Long attachmentId)
    {
        issueRepository.findById(issueId).ifPresent(issue -> {
            attachmentRepository.findById(attachmentId).ifPresent(attachment -> {
                if (user.isAdmin() || user.getId() == attachment.getCreatedBy().getId())
                {
                    attachmentRepository.delete(attachment);
                    dbFileLogic.deleteDBFile(attachment.getDbFile().getId());
                    IssueEvent issueEvent = issueEventRepository.save(new IssueEvent(0, user, issue, EventType.REMOVE, "attachment", attachment.getName()));
                    mailClient.prepareAndSend(issueEvent);
                    redirectAttrs.addFlashAttribute("responseMessage", "Attachment removed.");
                }
            });
        });

        return new ModelAndView("redirect:/issue/form?issueId=" + issueId);
    }
}
