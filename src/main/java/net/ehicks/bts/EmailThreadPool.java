package net.ehicks.bts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailThreadPool
{
    private static final Logger log = LoggerFactory.getLogger(EmailThreadPool.class);
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static ExecutorService getPool()
    {
        return pool;
    }
}
