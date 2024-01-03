package biblioexp.bibleo.Service;


import biblioexp.bibleo.Entity.Book;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface BookService {
    Book saveBook(Book Book);
    List<Book> getAllBooks();
    Book getBookById(long ISBN);
    Book updateBook(Book Book, long ISBN);
    void deleteBook(long ISBN);
    void increaseLoanedCopies(Book book);
    void decreaseLoanedCopies(Book book);
    void increaseReservedCopies(Book book);
    void decreaseReservedCopies(Book book);
    Optional<Book> findById(Long ISBN);
    List<Book> searchBooks(String query, String searchCriteria);
    List<Book> sortBooks(List<Book> books, String sortCriteria);

}