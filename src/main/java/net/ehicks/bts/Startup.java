package net.ehicks.bts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class Startup
{
    private static final Logger log = LoggerFactory.getLogger(Startup.class);

    @Value("${puffin.dropCreateLoad:0}")
    public String dropCreateLoad;

    @Value("classpath:net/ehicks/bts/beans")
    Resource beans;

    private Seeder seeder;

    public Startup(Seeder seeder)
    {
        this.seeder = seeder;
    }

    public void start()
    {
        log.info("BTS starting...");
        SystemInfo.INSTANCE.setSystemStart(System.currentTimeMillis());

        if (dropCreateLoad.toLowerCase().equals("true"))
        {
            seeder.createDemoData();
        }

//        DatabaseBackupTask.scheduleTask();
//        ChatSessionHandler.init(); // todo: move this?

        log.info("BTS Controller.init done in {} ms", (System.currentTimeMillis() - SystemInfo.INSTANCE.getSystemStart()));
    }
}
