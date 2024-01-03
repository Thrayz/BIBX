package biblioexp.bibleo.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "message")
    private String message;

    @Column(name = "notification_date")
    private Date notificationDate;

    @Column(name = "viewed")
    private boolean viewed;

    public Notification(User user, Book book, NotificationType notificationType, String message, Date notificationDate) {
        this.user = user;
        this.book = book;
        this.notificationType = notificationType;
        this.message = message;
        this.notificationDate = notificationDate;
    }

    public Notification() {

    }

    // Getters for all attributes

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getMessage() {
        return message;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public boolean isViewed() {
        return viewed;
    }

    // Setters for all attributes

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    // Additional constructors, if needed...

}
