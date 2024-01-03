package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Category;
import biblioexp.bibleo.Entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    @Query("SELECT l FROM Loan l WHERE l.returnDate >= current_date AND l.returnDate <= current_date + 3")
    List<Loan> findApproachingReturnLoans();

    @Query("SELECT l FROM Loan l WHERE l.returnDate < current_date")
    List<Loan> findOverdueLoans();
}
