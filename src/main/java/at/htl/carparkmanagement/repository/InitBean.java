package at.htl.carparkmanagement.repository;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class InitBean {
    @Inject
    Logger logger;

    void onStart(@Observes StartupEvent ev) {
        logger.log(Logger.Level.INFO, "The application is starting...");
    }

    void onStop(@Observes ShutdownEvent ev) {
        logger.log(Logger.Level.INFO, "The application is stopping...");
    }
}
