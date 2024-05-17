package biblioexp.bibleo.Service;

import biblioexp.bibleo.Entity.Book;

import java.util.List;
import java.util.Optional;


import biblioexp.bibleo.Entity.Book;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book saveBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(long ISBN);
    Book updateBook(Book updatedBook, long ISBN);
    void deleteBook(long ISBN);
    void increaseLoanedCopies(Book book);
    void decreaseLoanedCopies(Book book);
    void increaseReservedCopies(Book book);
    void decreaseReservedCopies(Book book);
    Optional<Book> findById(Long ISBN);
    List<Book> searchBooks(String query, String searchCriteria);
    List<Book> sortBooks(List<Book> books, String sortCriteria);

    ResponseEntity loadBookImage(String filename); // Define loadBookImage method
    String uploadBookImage(MultipartFile image, String filename);

    //void loadBooksFromCSV(String csvFilePath);

}
