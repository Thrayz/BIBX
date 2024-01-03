package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.*;
import biblioexp.bibleo.Service.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import biblioexp.bibleo.config.CustomLoginSucessHandler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/Books")
public class BookController {
    private final BookService BookService;
    private final UserService UserService;
    private final LoanService LoanService;
    private final ReservationService ReservationService;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    public BookController(BookService bookService, biblioexp.bibleo.Service.UserService userService, biblioexp.bibleo.Service.LoanService loanService, biblioexp.bibleo.Service.ReservationService reservationService) {
        this.BookService = bookService;
        this.UserService = userService;
        this.LoanService = loanService;
        this.ReservationService = reservationService;
    }
    @Autowired
    private CategoryService categoryService;
    // build create Book REST API
    @PostMapping()
    public ResponseEntity<Book> saveBook(@RequestBody Book Book) {
        return new
                ResponseEntity<Book>(BookService.saveBook(Book),
                HttpStatus.CREATED);
    }

    // build get all Books REST API
    @GetMapping
    public List<Book> getAllBooks() {
        return BookService.getAllBooks();
    }

    // build get Book by id REST API
// http://localhost:8080/api/Books/1
    @GetMapping("{ISBN}")
    public ResponseEntity<Book> getBookById(@PathVariable("ISBN")
                                            long ISBN) {
        return new
                ResponseEntity<Book>(BookService.getBookById(ISBN),
                HttpStatus.OK);
    }
    @GetMapping("/edit/{ISBN}")
    public ModelAndView showUpdateForm(@PathVariable("ISBN") Long ISBN)  {
        Book book = BookService.getBookById(ISBN);
        List<Category> categories = categoryService.getAllCategories();

        ModelAndView modelAndView = new ModelAndView("update-book");
        modelAndView.addObject("book", book);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @PostMapping("/update/{ISBN}")
    public ModelAndView updateBook(@PathVariable("ISBN") long ISBN,
                                   @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Book book,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            book.setISBN(ISBN);
            logger.error("Validation errors occurred for ISBN {}: {}", ISBN, result.getAllErrors());
            return new ModelAndView("redirect:/api/Books/edit/{ISBN}");
        }

        try {
            Book updatedBook = BookService.updateBook(book, ISBN);
            logger.info("Book updated successfully. ISBN: {}", updatedBook.getISBN());
        } catch (Exception e) {
            logger.error("Error updating book with ISBN " + ISBN, e);
            // Handle the exception as needed
        }

        return new ModelAndView("redirect:/api/Books/BookList");
    }
    // Delete User REST API
    // http://localhost:8080/api/users/1
    @GetMapping("/delete/{ISBN}")
    public ModelAndView deleteBook(@PathVariable("ISBN") long ISBN, Model model) {
        BookService.deleteBook(ISBN);
        return new ModelAndView("redirect:/api/Books/BookList");
    }

    @GetMapping("/BookList")
    public ModelAndView showBookList() {
        List<Book> BookList = BookService.getAllBooks();
        ModelAndView modelAndView = new ModelAndView("booklist");
        modelAndView.addObject("BookList", BookList);
        return modelAndView;

    }

    @PostMapping("/loan/{isbn}")
    public ModelAndView loanBook(@PathVariable("isbn") Long isbn, HttpServletRequest request) {
        Long userIdFromCookie = CustomLoginSucessHandler.getUserIDFromCookie(request);
        Book book = BookService.getBookById(isbn);
        User user = UserService.getUserById(userIdFromCookie);

        if (book.getAvb_copies() > 0) {
            Loan loan = new Loan(book, user, new Date(), calculateReturnDate(), LoanStatus.ACTIVE);
            LoanService.saveLoan(loan);

            book.setAvb_copies(book.getAvb_copies() - 1);
            BookService.increaseLoanedCopies(book);
            BookService.saveBook(book);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/api/Books/search");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/api/Books/search");
            return modelAndView;
        }
    }
    private Date calculateReturnDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 10);
        return calendar.getTime();
    }


    @PostMapping("/reserve/{isbn}")
    public ModelAndView reserveBook(@PathVariable("isbn") Long isbn, HttpServletRequest request) {
        Long userIdFromCookie = CustomLoginSucessHandler.getUserIDFromCookie(request);
        Book book = BookService.getBookById(isbn);
        User user = UserService.getUserById(userIdFromCookie);
        Reservation reservation = new Reservation(book, user, new Date());
        ReservationService.saveReservation(reservation);
        BookService.increaseReservedCopies(book);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/api/Books/search");
        return modelAndView;
    }
    @GetMapping("/search")
    public ModelAndView searchAndSortBooks(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "criteria", required = false, defaultValue = "title") String searchCriteria,
            @RequestParam(name = "sort", required = false) String sortCriteria) {

        List<Book> result;

        if (query != null && !query.isEmpty()) {
            result = BookService.searchBooks(query, searchCriteria);
        } else {
            result = BookService.getAllBooks();
        }

        if (sortCriteria != null && !sortCriteria.isEmpty()) {
            result = BookService.sortBooks(result, sortCriteria);
        }

        List<Category> categories = categoryService.getAllCategories();

        ModelAndView modelAndView = new ModelAndView("Catalogue");
        modelAndView.addObject("BookList", result);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }




    @GetMapping("/add")
    public ModelAndView showAddBookForm() {
        List<Category> categories = categoryService.getAllCategories();
        ModelAndView modelAndView = new ModelAndView("add-book");
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("book", new Book());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addBook(@ModelAttribute("book") Book book) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Book addedBook = BookService.saveBook(book);
            modelAndView.setViewName("redirect:/api/Books/list");
        } catch (Exception e) {
            modelAndView.setViewName("add-book");
            modelAndView.addObject("error", "An error occurred while adding the book.");
        }
        return modelAndView;
    }

}