/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author berk
 */

public class SchedulerServiceListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        //scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new MailSenderJob(), 1, 5, TimeUnit.MINUTES);
        //scheduler.scheduleAtFixedRate(new YourParsingJob(), 0, 5, TimeUnit.HOUR);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
