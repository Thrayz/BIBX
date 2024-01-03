package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Book;

import biblioexp.bibleo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long>{
    Optional<Book> findById(Long bookId);
    List<Book> findByTitleContainingIgnoreCaseOrderByTitle(String title);
    List<Book> findByAuthorContainingIgnoreCaseOrderByAuthor(String author);

    List<Book> findByCategory_CategoryNameContainingIgnoreCaseOrderByTitle(String categoryName);


}
