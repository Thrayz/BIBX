package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.*;
import biblioexp.bibleo.Service.*;
import org.springframework.util.StreamUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import biblioexp.bibleo.config.CustomLoginSucessHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/Books")
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;
    private final ReservationService reservationService;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private CategoryService categoryService;

    public BookController(BookService bookService, UserService userService, LoanService loanService, ReservationService reservationService) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
        this.reservationService = reservationService;
    }

    @PostMapping("/uploadImage/{ISBN}")
    public ResponseEntity<String> uploadImage(@PathVariable long ISBN, @RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        String imageUrl = bookService.uploadBookImage(file, fileName);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/books/images/")
                .path(fileName)
                .toUriString();

        return ResponseEntity.ok(fileDownloadUri);
    }








    @PostMapping()
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        return new ResponseEntity<Book>(bookService.saveBook(book), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("{ISBN}")
    public ResponseEntity<Book> getBookById(@PathVariable("ISBN") long ISBN) {
        return new ResponseEntity<Book>(bookService.getBookById(ISBN), HttpStatus.OK);
    }

    @GetMapping("/edit/{ISBN}")
    public ModelAndView showUpdateForm(@PathVariable("ISBN") Long ISBN) {
        Book book = bookService.getBookById(ISBN);
        List<Category> categories = categoryService.getAllCategories();
        ModelAndView modelAndView = new ModelAndView("update-book");
        modelAndView.addObject("Book", book);
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
            return new ModelAndView("redirect:/api/books/edit/{ISBN}");
        }
        try {
            Book updatedBook = bookService.updateBook(book, ISBN);
            logger.info("Book updated successfully. ISBN: {}", updatedBook.getISBN());
        } catch (Exception e) {
            logger.error("Error updating book with ISBN " + ISBN, e);
        }
        return new ModelAndView("redirect:/api/books/bookList");
    }

    @GetMapping("/delete/{ISBN}")
    public ModelAndView deleteBook(@PathVariable("ISBN") long ISBN, Model model) {
        bookService.deleteBook(ISBN);
        return new ModelAndView("redirect:/api/Books/BookList");
    }

    @GetMapping("/BookList")
    public ModelAndView showBookList() {
        List<Book> BookList = bookService.getAllBooks();
        System.out.println("Book List: " + BookList); // Add this line to log the bookList

        ModelAndView modelAndView = new ModelAndView("Catalogue");
        modelAndView.addObject("BookList", BookList);
        return modelAndView;
    }

    @PostMapping("/loan/{isbn}")
    public ModelAndView loanBook(@PathVariable("isbn") Long isbn, HttpServletRequest request) {
        Long userIdFromCookie = CustomLoginSucessHandler.getUserIDFromCookie(request);
        Book book = bookService.getBookById(isbn);
        User user = userService.getUserById(userIdFromCookie);
        if (book.getAvb_copies() > 0) {
            Loan loan = new Loan(book, user, new Date(), calculateReturnDate(), LoanStatus.ACTIVE);
            loanService.saveLoan(loan);
            book.setAvb_copies(book.getAvb_copies() - 1);
            bookService.increaseLoanedCopies(book);
            bookService.saveBook(book);
            return new ModelAndView("redirect:/api/Books/search");
        } else {
            return new ModelAndView("redirect:/api/Books/search");
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
        Book book = bookService.getBookById(isbn);
        User user = userService.getUserById(userIdFromCookie);
        Reservation reservation = new Reservation(book, user, new Date());
        reservationService.saveReservation(reservation);
        bookService.increaseReservedCopies(book);
        return new ModelAndView("redirect:/api/Books/search");
    }

    @GetMapping("/search")
    public ModelAndView searchAndSortBooks(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "criteria", required = false, defaultValue = "title") String searchCriteria,
            @RequestParam(name = "sort", required = false) String sortCriteria) {

        List<Book> result;

        if (query != null && !query.isEmpty()) {
            result = bookService.searchBooks(query, searchCriteria);
        } else {
            result = bookService.getAllBooks();
        }

        if (sortCriteria != null && !sortCriteria.isEmpty()) {
            result = bookService.sortBooks(result, sortCriteria);
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
        modelAndView.addObject("Book", new Book());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addBook(@ModelAttribute("book") Book book,
                                @RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            // Save the uploaded image file
            String imageUrl = bookService.uploadBookImage(file, file.getOriginalFilename());
            book.setImageUrl(imageUrl); // Set the image URL in the book object

            // Save the book entity
            Book addedBook = bookService.saveBook(book);

            // Redirect to the book list page
            modelAndView.setViewName("redirect:/api/books/list");
        } catch (Exception e) {
            modelAndView.setViewName("add-book");
            modelAndView.addObject("error", "An error occurred while adding the book.");
        }
        return modelAndView;
    }}
