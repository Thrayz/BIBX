package biblioexp.bibleo.Service;

import biblioexp.bibleo.Entity.Notification;

public interface NotificationService {
    void notifyApproachingReturnDeadline();
    void notifyOverdueBooks();
    void notifyReservedBooksAvailable();
    void sendNotification(Notification notification);
    void markNotificationAsViewed(long notificationId);
}
