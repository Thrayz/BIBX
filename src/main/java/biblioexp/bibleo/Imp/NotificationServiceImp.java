package biblioexp.bibleo.Imp;

import biblioexp.bibleo.Controller.ReservationRepository;
import biblioexp.bibleo.Entity.*;
import biblioexp.bibleo.Controller.NotificationRepository;
import biblioexp.bibleo.Service.NotificationService;
import biblioexp.bibleo.Service.ReservationService;
import biblioexp.bibleo.Service.LoanService;
import biblioexp.bibleo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImp implements NotificationService {

    private final LoanService loanService;
    private final ReservationService reservationService;
    private NotificationRepository NotificationRepository;

    public NotificationServiceImp(LoanService loanService, ReservationService reservationService, NotificationRepository NotificationRepository) {
        super();
        this.NotificationRepository = NotificationRepository;
        this.loanService = loanService;
        this.reservationService = reservationService;
    }

    @Override
    public void notifyApproachingReturnDeadline() {
        List<Loan> approachingReturnLoans = loanService.findApproachingReturnLoans();

        for (Loan loan : approachingReturnLoans) {
            User user = loan.getUser();
            Book book = loan.getBook();
            Notification notification = new Notification(user, book, NotificationType.APPROACHING_RETURN_DEADLINE, "Approaching return deadline for book: " + book.getTitle(), new Date());
            sendNotification(notification);
        }
    }

    @Override
    public void notifyOverdueBooks() {

        List<Loan> overdueLoans = loanService.findOverdueLoans();

        for (Loan loan : overdueLoans) {
            User user = loan.getUser();
            Book book = loan.getBook();
            Notification notification = new Notification(user, book, NotificationType.OVERDUE_BOOK, "Overdue book: " + book.getTitle(), new Date());
            sendNotification(notification);
        }
    }

    @Override
    public void notifyReservedBooksAvailable() {

        List<Reservation> reservedBooksAvailable = reservationService.findReservedBooksAvailable();

        for (Reservation reservation : reservedBooksAvailable) {
            User user = reservation.getUser();
            Book book = reservation.getBook();
            Notification notification = new Notification(user, book, NotificationType.RESERVED_BOOK_AVAILABLE, "Reserved book now available: " + book.getTitle(), new Date());
            sendNotification(notification);
        }
    }



    @Override
    public void sendNotification(Notification notification) {
        notification.setViewed(false);
        NotificationRepository.save(notification);
    }

    @Override
    public void markNotificationAsViewed(long notificationId) {
        Notification notification = NotificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "Id", notificationId));

        notification.setViewed(true);
        NotificationRepository.save(notification);
    }
}
