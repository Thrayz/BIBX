package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Category;
import biblioexp.bibleo.Service.CategoryService;




import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/Categories")
public class CategoryController {
    private CategoryService CategoryService;
    public CategoryController(CategoryService categoryService) {
        super();
        this.CategoryService = categoryService;
    }
    // build create Category REST API
    @PostMapping()
    public ResponseEntity<Category> saveCategory(@RequestBody Category Category){
        return new
                ResponseEntity<Category>(CategoryService.saveCategory(Category),
                HttpStatus.CREATED);
    }
    // build get all Categorys REST API
    @GetMapping
    public List<Category> getAllCategories(){
        return CategoryService.getAllCategories();
    }
    // build get Category by id REST API
// http://localhost:8080/api/Categorys/1
    @GetMapping("{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id")
                                                    long CategoryId){
        return new
                ResponseEntity<Category>(CategoryService.getCategoryById(CategoryId),
                HttpStatus.OK);
    }
    // build update Category REST API
// http://localhost:8080/api/Categorys/1
    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id")
                                                   long id
            ,@RequestBody Category Category){
        return new
                ResponseEntity<Category>(CategoryService.updateCategory(Category, id),
                HttpStatus.OK);
    }
    // build delete Category REST API
// http://localhost:8080/api/Categorys/1
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") long
                                                         id){
// delete Category from DB
        CategoryService.deleteCategory(id);
        return new ResponseEntity<String>("Category deleted successfully!.", HttpStatus.OK);
    }

    @GetMapping("/CategoryList")
    public ModelAndView showCategoryList(Model model) {
        List<Category> categoryList = CategoryService.getAllCategories();
        ModelAndView modelAndView = new ModelAndView("categoryList");
        model.addAttribute("categoryList", categoryList);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer categoryId) {
        Category category = CategoryService.getCategoryById(categoryId);

        ModelAndView modelAndView = new ModelAndView("update-category");
        modelAndView.addObject("category", category);

        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateCategory(@PathVariable("id") Integer categoryId,
                                       @Valid @ModelAttribute("category") Category category,
                                       BindingResult result) {
        if (result.hasErrors()) {
            // Handle validation errors as needed
            ModelAndView errorModelAndView = new ModelAndView("update-category");
            errorModelAndView.addObject("category", category);
            return errorModelAndView;
        }

        Category updatedCategory = CategoryService.updateCategory(category, categoryId);

        ModelAndView successModelAndView = new ModelAndView("redirect:/api/Categories/CategoryList");
        return successModelAndView;
    }


    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer categoryId) {
        CategoryService.deleteCategory(categoryId);
        return "redirect:/api/Categories/CategoryList";
    }

    @GetMapping("/add")
    public ModelAndView showAddCategoryForm() {
        ModelAndView modelAndView = new ModelAndView("add-category");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addCategory(@Valid @ModelAttribute("category") Category category, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.setViewName("add-category");
            modelAndView.addObject("error", "An error occurred while adding the category.");
            return modelAndView;
        }

        try {
            Category addedCategory = CategoryService.saveCategory(category);
            modelAndView.setViewName("redirect:/api/Categories/CategoryList");
        } catch (Exception e) {
            modelAndView.setViewName("add-category");
            modelAndView.addObject("error", "An error occurred while adding the category.");
        }

        return modelAndView;
    }


}
