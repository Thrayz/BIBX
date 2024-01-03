package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}