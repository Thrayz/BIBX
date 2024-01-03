package biblioexp.bibleo.Service;

import biblioexp.bibleo.Controller.CategoryRepository;
import biblioexp.bibleo.Entity.Category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    Category saveCategory(Category Category);
    List<Category> getAllCategories();
    Category getCategoryById(long id);
    Category updateCategory(Category Category, long id);
    void deleteCategory(long id);
}