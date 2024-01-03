package biblioexp.bibleo.Imp;

import biblioexp.bibleo.Entity.Book;
import biblioexp.bibleo.Service.BookService;
import biblioexp.bibleo.exception.ResourceNotFoundException;

import biblioexp.bibleo.Controller.BookRepository;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImp implements BookService{
    private BookRepository BookRepository;
    public BookServiceImp(BookRepository BookRepository) {
        super();
        this.BookRepository = BookRepository;
    }
    @Override
    public Book saveBook(Book Book) {
        return BookRepository.save(Book);
    }
    @Override
    public List<Book> getAllBooks() {
        return BookRepository.findAll();
    }
    @Override
    public Book getBookById(long ISBN) {
// Optional<Book> Book = BookRepository.findById(id);
// if(Book.isPresent()) {
// return Book.get();
// }else {
// throw new ResourceNotFoundException("Book", "Id", id);
// }
        return BookRepository.findById(ISBN).orElseThrow(() ->
                new
                        ResourceNotFoundException("Book", "ISBN", ISBN));
    }
    @Override
    public Book updateBook(Book updatedBook, long ISBN) {
        Book existingBook = BookRepository.findById(ISBN)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "ISBN", ISBN));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setNbr_copies(updatedBook.getNbr_copies());
        existingBook.setAvb_copies(updatedBook.getAvb_copies());
        existingBook.setDate_pub(updatedBook.getDate_pub());
        existingBook.setCategory(updatedBook.getCategory());

        return BookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(long ISBN) {
// check whether a Book exist in a DB or not
        BookRepository.findById(ISBN).orElseThrow(() ->
                new
                        ResourceNotFoundException("Book", "ISBN", ISBN));
        BookRepository.deleteById(ISBN);
    }

    @Override
    public Optional<Book> findById(Long ISBN) {
        return BookRepository.findById(ISBN);
    }
    @Override
    public List<Book> searchBooks(String query, String searchCriteria) {
        List<Book> result;

        switch (searchCriteria) {
            case "title":
                result = BookRepository.findByTitleContainingIgnoreCaseOrderByTitle(query);
                break;
            case "author":
                result = BookRepository.findByAuthorContainingIgnoreCaseOrderByAuthor(query);
                break;
            case "category":
                result = BookRepository.findByCategory_CategoryNameContainingIgnoreCaseOrderByTitle(query);
                break;
            default:
                // Default to searching by title
                result = BookRepository.findByTitleContainingIgnoreCaseOrderByTitle(query);
                break;
        }

        return result;
    }
    // In BookServiceImp
    @Override
    public List<Book> sortBooks(List<Book> books, String sortCriteria) {
        switch (sortCriteria) {
            case "title":
                books.sort(Comparator.comparing(Book::getTitle));
                break;
            case "author":
                books.sort(Comparator.comparing(Book::getAuthor));
                break;

            default:

                books.sort(Comparator.comparing(Book::getTitle));
                break;
        }
        return books;
    }

    @Override
    public void increaseLoanedCopies(Book book) {
        book.setLoanedCopies(book.getLoanedCopies() + 1);
        saveBook(book);
    }

    @Override
    public void decreaseLoanedCopies(Book book) {
        book.setLoanedCopies(book.getLoanedCopies() - 1);
        saveBook(book);
    }

    @Override
    public void increaseReservedCopies(Book book) {
        book.setReservedCopies(book.getReservedCopies() + 1);
        saveBook(book);
    }

    @Override
    public void decreaseReservedCopies(Book book) {
        book.setReservedCopies(book.getReservedCopies() - 1);
        saveBook(book);
    }

}
