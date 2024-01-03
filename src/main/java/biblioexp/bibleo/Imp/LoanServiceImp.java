package biblioexp.bibleo.Imp;

import biblioexp.bibleo.Controller.LoanRepository;
import biblioexp.bibleo.Entity.Book;
import biblioexp.bibleo.Entity.Category;
import biblioexp.bibleo.Entity.Loan;
import biblioexp.bibleo.Entity.LoanStatus;
import biblioexp.bibleo.Service.BookService;
import biblioexp.bibleo.Service.LoanService;
import biblioexp.bibleo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service

public class LoanServiceImp implements LoanService {
    private LoanRepository LoanRepository;
    private final BookService bookService;

    @Autowired
    public LoanServiceImp(LoanRepository loanRepository, BookService bookService) {
        this.LoanRepository = loanRepository;
        this.bookService = bookService;
    }
    @Override
    public Loan saveLoan(Loan Loan) {
        return LoanRepository.save(Loan);
    }

    @Override
    public List<Loan> getAllLoans() {
        return LoanRepository.findAll();
    }

    @Override
    public Loan getLoanById(long id) {
        return LoanRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Loan",
                                "Id", id));
    }

    @Override
    public Loan UpdateLoan(Loan Loan, long id) {
        Loan existingLoan =
                LoanRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Loan",
                                "Id", id));
        existingLoan.setUser(existingLoan.getUser());
        existingLoan.setBook(existingLoan.getBook());
        existingLoan.setBorrowDate(existingLoan.getBorrowDate());
        existingLoan.setReturnDate(existingLoan.getReturnDate());
// save existing Category to DB
        LoanRepository.save(existingLoan);
        return existingLoan;
    }

    @Override
    public void DeleteLoan(long id) {
       LoanRepository.findById(id).orElseThrow(() ->
                new
                        ResourceNotFoundException("Loan", "Id", id));
       LoanRepository.deleteById(id);

    }

    @Override
    public List<Loan> findApproachingReturnLoans() {

        return LoanRepository.findApproachingReturnLoans();
    }

    @Override
    public List<Loan> findOverdueLoans() {

        return LoanRepository.findOverdueLoans();
    }

    @Override
    public Loan renewLoan(long loanId) {
        Loan loan = LoanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "Id", loanId));

        if (loan.getStatus() == LoanStatus.ACTIVE) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(loan.getReturnDate());
            calendar.add(Calendar.DAY_OF_MONTH, 10);
            loan.setReturnDate(calendar.getTime());

            LoanRepository.save(loan);
        }

        return loan;
    }

    @Override
    public Loan returnLoan(long loanId) {
        Loan loan = LoanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "Id", loanId));

        if (loan.getStatus() == LoanStatus.ACTIVE) {
            loan.setStatus(LoanStatus.RETURNED);
            loan.setReturnDate(new Date());

            LoanRepository.save(loan);


            Book returnedBook = loan.getBook();
            returnedBook.setAvb_copies(returnedBook.getAvb_copies() + 1);
            bookService.saveBook(returnedBook);
        }

        return loan;
    }



}
