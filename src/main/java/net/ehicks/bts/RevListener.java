package net.ehicks.bts;

import net.ehicks.bts.beans.User;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RevListener implements RevisionListener {
    public void newRevision(Object revisionEntity) {
        RevisionInfo revisionInfo = (RevisionInfo) revisionEntity;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication == null)
            username = "SYSTEM";
        else if (authentication instanceof AnonymousAuthenticationToken) {
            username = "ANONYMOUS";
        }
        else {
            User user = (User) authentication.getPrincipal();
            username = String.valueOf(user.getId());
        }

        revisionInfo.setUsername(username);
    }
}