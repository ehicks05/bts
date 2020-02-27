package net.ehicks.bts.mail;

import com.sksamuel.diffpatch.DiffMatchPatch;
import net.ehicks.bts.beans.EmailEvent;
import net.ehicks.bts.beans.EventType;
import net.ehicks.bts.beans.Issue;
import net.ehicks.bts.beans.User;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.LinkedList;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildContent(EmailEvent emailEvent) {
        User user = emailEvent.getUser();
        Issue issue = emailEvent.getIssue();
        EventType eventType = emailEvent.getEventType();

        String emailContext = "http://localhost:8082"; // todo figure this out

        String titleLink = emailContext + "/issue/form?issueId=" + issue.getId();
        String titleText = issue.getProject().getPrefix() + "-" + issue.getId() + " " + issue.getTitle();
        String userAvatarSource = emailContext + "/avatar/" + user.getAvatar().getId();
        String userProfileLink = emailContext + "/profile/form?profileUserId=" + user.getId();
        String userProfileText = user.getUsername();
        String summary = eventType.getVerb() + " " + emailEvent.getPropertyName() + ":";

        LinkedList<DiffMatchPatch.Diff> diffs = new LinkedList<>();
        if (eventType == EventType.ADD)
            diffs.add(new DiffMatchPatch.Diff(DiffMatchPatch.Operation.INSERT, emailEvent.getNewValue()));
        if (eventType == EventType.UPDATE)
        {
            if (Arrays.asList("title", "description", "comment").contains(emailEvent.getPropertyName()))
            {
                DiffMatchPatch myDiff = new DiffMatchPatch();
                diffs = myDiff.diff_main(emailEvent.getOldValue(), emailEvent.getNewValue());
                myDiff.diff_cleanupSemantic(diffs);
            }
            else
            {
                diffs.add(new DiffMatchPatch.Diff(DiffMatchPatch.Operation.DELETE, emailEvent.getOldValue()));
                diffs.add(new DiffMatchPatch.Diff(DiffMatchPatch.Operation.INSERT, emailEvent.getNewValue()));
            }
        }
        if (eventType == EventType.REMOVE)
            diffs.add(new DiffMatchPatch.Diff(DiffMatchPatch.Operation.DELETE, emailEvent.getOldValue()));

        Context context = new Context();
        context.setVariable("titleLink", titleLink);
        context.setVariable("titleText", titleText);
        context.setVariable("userAvatarSource", userAvatarSource);
        context.setVariable("userProfileLink", userProfileLink);
        context.setVariable("userProfileText", userProfileText);
        context.setVariable("summary", summary);
        context.setVariable("diffs", diffs);

        return templateEngine.process("mailTemplate", context);
    }
}