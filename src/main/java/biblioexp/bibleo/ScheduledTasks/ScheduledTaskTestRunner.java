package biblioexp.bibleo.ScheduledTasks;

import biblioexp.bibleo.ScheduledTasks.ScheduledTasks;
import biblioexp.bibleo.ScheduledTasks.NotificationScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskTestRunner implements CommandLineRunner {

    private final ScheduledTasks scheduledTasks;
    private final NotificationScheduler notificationScheduler;

    @Autowired
    public ScheduledTaskTestRunner(ScheduledTasks scheduledTasks, NotificationScheduler notificationScheduler) {
        this.scheduledTasks = scheduledTasks;
        this.notificationScheduler = notificationScheduler;
    }

    @Override
    public void run(String... args) {
        scheduledTasks.checkLoansAndReservations();
        notificationScheduler.notifyApproachingReturnDeadline();
        notificationScheduler.notifyOverdueBooks();
        notificationScheduler.notifyReservedBooksAvailable();
    }
}
