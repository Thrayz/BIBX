package biblioexp.bibleo.Service;

import biblioexp.bibleo.Entity.Loan;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoanService {

    Loan saveLoan(Loan loan);

    List<Loan> getAllLoans();
    Loan getLoanById(long id);
    Loan UpdateLoan(Loan Loan, long id);
    void DeleteLoan(long id);

    List<Loan> findApproachingReturnLoans();

    List<Loan> findOverdueLoans();
    Loan renewLoan(long loanId);

    Loan returnLoan(long loanId);

}
