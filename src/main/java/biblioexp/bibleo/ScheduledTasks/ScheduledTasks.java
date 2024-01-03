// ScheduledTasks.java
package biblioexp.bibleo.ScheduledTasks;

import biblioexp.bibleo.Entity.Loan;
import biblioexp.bibleo.Entity.Reservation;
import biblioexp.bibleo.Service.LoanService;
import biblioexp.bibleo.Service.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    private final LoanService loanService;
    private final ReservationService reservationService;

    public ScheduledTasks(LoanService loanService, ReservationService reservationService) {
        this.loanService = loanService;
        this.reservationService = reservationService;
    }

   // @Scheduled(cron = "0 0 0 * * ?")
    public void checkLoansAndReservations() {
        List<Loan> approachingReturnLoans = loanService.findApproachingReturnLoans();
        List<Loan> overdueLoans = loanService.findOverdueLoans();
        List<Reservation> reservedBooksAvailable = reservationService.findReservedBooksAvailable();
    }
}
