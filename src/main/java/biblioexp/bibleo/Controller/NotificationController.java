package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Notification;
import biblioexp.bibleo.Service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
/*
    // Endpoint to get all notifications
    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    // Endpoint to get a specific notification by ID
    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable long id) {
        return notificationService.getNotificationById(id);
    }

    // Endpoint to create a new notification
    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationService.createNotification(notification);
    }

    // Endpoint to update an existing notification
    @PutMapping("/{id}")
    public Notification updateNotification(@PathVariable long id, @RequestBody Notification notification) {
        return notificationService.updateNotification(id, notification);
    }

    // Endpoint to delete a notification by ID
    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable long id) {
        notificationService.deleteNotification(id);
    }*/
}
