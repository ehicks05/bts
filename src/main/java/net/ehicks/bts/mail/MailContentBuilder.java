package net.ehicks.bts.mail;

import com.sksamuel.diffpatch.DiffMatchPatch;
import net.ehicks.bts.beans.EmailEvent;
import net.ehicks.bts.beans.Issue;
import net.ehicks.bts.beans.User;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildContent(EmailEvent emailEvent) {
        User user = emailEvent.getUser();
        Issue issue = emailEvent.getIssue();
        String description = emailEvent.getDescription();
        Long actionId = emailEvent.getActionId();
        LinkedList<DiffMatchPatch.Diff> diffs = null;

        String emailContext = "http://localhost:8080/bts"; // todo figure this out

        String titleLink = "";
        String titleText = "";
        String userAvatarSource = "";
        String h3Text = "";
        String verb = EmailAction.getById(actionId).getVerb();

        if (actionId == EmailAction.ADD_COMMENT.getId() || actionId == EmailAction.EDIT_COMMENT.getId())
        {
            titleLink = emailContext + "/issue/form&issueId=" + issue.getId();
            titleText = issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle();
            userAvatarSource = emailContext + "/avatar/" + user.getAvatar().getId();
            h3Text = user.getUsername() + " " + verb + ".";

            if (actionId == EmailAction.EDIT_COMMENT.getId())
            {
                DiffMatchPatch myDiff = new DiffMatchPatch();
                diffs = myDiff.diff_main(emailEvent.getPreviousValue(), emailEvent.getNewValue());
                myDiff.diff_cleanupSemantic(diffs);
            }
        }
        if (actionId == EmailAction.TEST.getId())
        {
            titleLink = emailContext;
            titleText = "Test Email";
            userAvatarSource = "";
            h3Text = "This is a test email.";
            description = "Sent from bts...";
        }

        Context context = new Context();
        context.setVariable("titleLink", titleLink);
        context.setVariable("titleText", titleText);
        context.setVariable("userAvatarSource", userAvatarSource);
        context.setVariable("h3Text", h3Text);
        context.setVariable("description", description);
        context.setVariable("diffs", diffs);

        return templateEngine.process("mailTemplate", context);
    }
}