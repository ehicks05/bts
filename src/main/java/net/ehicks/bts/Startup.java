package net.ehicks.bts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Startup
{
    private static final Logger log = LoggerFactory.getLogger(Startup.class);

    @Value("${puffin.seedDbIfEmpty:false}")
    public String seedDbIfEmpty;

    private Seeder seeder;

    public Startup(Seeder seeder)
    {
        this.seeder = seeder;
    }

    public void start()
    {
        log.info("Puffin starting...");

        seeder.seed(seedDbIfEmpty.toLowerCase().equals("true"));
//        DatabaseBackupTask.scheduleTask();
//        ChatSessionHandler.init(); // todo: move this?
    }
}
