package biblioexp.bibleo.Imp;

import biblioexp.bibleo.Entity.Category;
import biblioexp.bibleo.Service.CategoryService;
import biblioexp.bibleo.exception.ResourceNotFoundException;

import biblioexp.bibleo.Controller.CategoryRepository;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class CategoryServiceImp implements CategoryService{
    private CategoryRepository CategoryRepository;
    public CategoryServiceImp(CategoryRepository CategoryRepository) {
        super();
        this.CategoryRepository = CategoryRepository;
    }
    @Override
    public Category saveCategory(Category Category) {
        return CategoryRepository.save(Category);
    }
    @Override
    public List<Category> getAllCategories() {
        return CategoryRepository.findAll();
    }
    @Override
    public Category getCategoryById(long id) {
// Optional<Category> Category = CategoryRepository.findById(id);
// if(Category.isPresent()) {
// return Category.get();
// }else {
// throw new ResourceNotFoundException("Category", "Id", id);
// }
        return CategoryRepository.findById(id).orElseThrow(() ->
                new
                        ResourceNotFoundException("Category", "Id", id));
    }
    @Override
    public Category updateCategory(Category Category, long id) {
// we need to check whether Category with given id is exist in
        //DB or not
        Category existingCategory =
                CategoryRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Category",
                                "Id", id));
        existingCategory.setCategoryName(Category.getCategoryName());
// save existing Category to DB
        CategoryRepository.save(existingCategory);
        return existingCategory;
    }
    @Override
    public void deleteCategory(long id) {
// check whether a Category exist in a DB or not
        CategoryRepository.findById(id).orElseThrow(() ->
                new
                        ResourceNotFoundException("Category", "Id", id));
        CategoryRepository.deleteById(id);
    }
}