package net.ehicks.bts.mail;

import net.ehicks.bts.beans.IssueEvent;
import net.ehicks.bts.beans.RenderMap;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildContent(IssueEvent issueEvent) {
        RenderMap renderMap = issueEvent.getRenderMap();

        Context context = new Context();
        context.setVariable("titleLink", renderMap.getTitleLink());
        context.setVariable("titleText", renderMap.getTitleText());
        context.setVariable("userAvatarSource", renderMap.getUserAvatarSource());
        context.setVariable("userProfileLink", renderMap.getUserProfileLink());
        context.setVariable("userProfileText", renderMap.getUserProfileText());
        context.setVariable("summary", renderMap.getSummary());
        context.setVariable("diffs", renderMap.getDiffs());

        return templateEngine.process("mailTemplate", context);
    }
}