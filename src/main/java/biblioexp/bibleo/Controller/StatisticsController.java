package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Book;
import biblioexp.bibleo.Service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatisticsController {

    private final BookService bookService;

    public StatisticsController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        List<Book> allBooks = bookService.getAllBooks();

        Map<String, Integer> loanedBooksByCategory = calculateLoanedBooksByCategory(allBooks);
        Map<String, Integer> reservedBooksByCategory = calculateReservedBooksByCategory(allBooks);
        Map<String, Integer> loanedBooksByBook = calculateLoanedBooksByBook(allBooks);
        Map<String, Integer> reservedBooksByBook = calculateReservedBooksByBook(allBooks);

        model.addAttribute("loanedBooksByCategory", loanedBooksByCategory);
        model.addAttribute("reservedBooksByCategory", reservedBooksByCategory);
        model.addAttribute("loanedBooksByBook", loanedBooksByBook);
        model.addAttribute("reservedBooksByBook", reservedBooksByBook);

        return "statistics";
    }

    // Existing methods...

    private Map<String, Integer> calculateLoanedBooksByBook(List<Book> books) {
        Map<String, Integer> result = new HashMap<>();

        for (Book book : books) {
            String title = book.getTitle();
            int loanedCount = book.getLoans().size();
            result.put(title, loanedCount);
        }

        return result;
    }

    private Map<String, Integer> calculateReservedBooksByBook(List<Book> books) {
        Map<String, Integer> result = new HashMap<>();

        for (Book book : books) {
            String title = book.getTitle();
            int reservedCount = book.getReservations().size();
            result.put(title, reservedCount);
        }

        return result;
    }

    private Map<String, Integer> calculateLoanedBooksByCategory(List<Book> books) {
        Map<String, Integer> result = new HashMap<>();

        for (Book book : books) {
            String category = book.getCategory().getCategoryName();
            int currentCount = result.getOrDefault(category, 0);
            int loanedCount = book.getLoans().size();
            result.put(category, currentCount + loanedCount);
        }

        return result;
    }

    private Map<String, Integer> calculateReservedBooksByCategory(List<Book> books) {
        Map<String, Integer> result = new HashMap<>();

        for (Book book : books) {
            String category = book.getCategory().getCategoryName();
            int currentCount = result.getOrDefault(category, 0);
            int reservedCount = book.getReservations().size();
            result.put(category, currentCount + reservedCount);
        }

        return result;
    }



}
