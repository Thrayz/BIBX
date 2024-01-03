package biblioexp.bibleo.ScheduledTasks;

import biblioexp.bibleo.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    private final NotificationService notificationService;

    @Autowired
    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //@Scheduled(cron = "0 0 0 * * *")
    public void notifyApproachingReturnDeadline() {
        notificationService.notifyApproachingReturnDeadline();
    }

    //@Scheduled(cron = "0 0 0 * * *")
    public void notifyOverdueBooks() {
        notificationService.notifyOverdueBooks();
    }

    //@Scheduled(cron = "0 0 0 * * *")
    public void notifyReservedBooksAvailable() {
        notificationService.notifyReservedBooksAvailable();
    }
}
