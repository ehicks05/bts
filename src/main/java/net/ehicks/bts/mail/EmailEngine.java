package net.ehicks.bts.mail;

import net.ehicks.bts.beans.*;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class EmailEngine
{
    private static final Logger log = LoggerFactory.getLogger(EmailEngine.class);

    SubscriptionRepository subscriptionRepository;

    public EmailEngine(SubscriptionRepository subscriptionRepository)
    {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void sendEmail(EmailEvent emailEvent)
    {
        // determine recipients


        // create email
        
    }
}
