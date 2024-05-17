package biblioexp.bibleo.Imp;

import biblioexp.bibleo.Entity.Book;
import biblioexp.bibleo.Entity.Category;
import biblioexp.bibleo.Service.BookService;
import biblioexp.bibleo.exception.ResourceNotFoundException;
import biblioexp.bibleo.Controller.BookRepository;

import biblioexp.bibleo.util.RandomDataGenerator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import biblioexp.bibleo.Service.CategoryService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImp implements BookService {
    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "classpath:images";
    @Value("${upload.path}")
    private String uploadPath;
    private final BookRepository bookRepository;

    public BookServiceImp(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    @Override
    public ResponseEntity<Resource> loadBookImage(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build(); // Return 404 Not Found status
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found: " + fileName, ex);
        }
    }
    @Override
    public Book getBookById(long ISBN) {
        return bookRepository.findById(ISBN).orElseThrow(() -> new ResourceNotFoundException("Book", "ISBN", ISBN));
    }
    /*
    @Override
    public void loadBooksFromCSV(String csvFilePath) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            // Skip the first row (headers)
            reader.skip(1);
            List<String[]> rows = reader.readAll();

            for (String[] row : rows) {
                // Assuming the CSV file has columns in the order: title, category, price, image_url
                String title = row[0];
                String category = row[1];

                String imageUrl = row[3];

                // Generate random data for other columns
                String author = RandomDataGenerator.generateRandomAuthor();
                long copies = RandomDataGenerator.generateRandomLong(100) + 1;
                String publishedDate = RandomDataGenerator.generateRandomDate();

                // Create a new Book object with the parsed and random data
                Category bookCategory = categoryService.getCategoryByName(category);
                Book book = new Book(title, author, copies, copies, publishedDate, bookCategory);
                book.setImageUrl(imageUrl);

                // Save the book to the database or perform any other required action
                saveBook(book);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }*/




    @Override
    public Book updateBook(Book updatedBook, long ISBN) {
        Book existingBook = bookRepository.findById(ISBN)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "ISBN", ISBN));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setNbr_copies(updatedBook.getNbr_copies());
        existingBook.setAvb_copies(updatedBook.getAvb_copies());
        existingBook.setDate_pub(updatedBook.getDate_pub());
        existingBook.setCategory(updatedBook.getCategory());

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(long ISBN) {
        bookRepository.findById(ISBN).orElseThrow(() -> new ResourceNotFoundException("Book", "ISBN", ISBN));
        bookRepository.deleteById(ISBN);
    }

    @Override
    public Optional<Book> findById(Long ISBN) {
        return bookRepository.findById(ISBN);
    }

    @Override
    public List<Book> searchBooks(String query, String searchCriteria) {
        switch (searchCriteria) {
            case "title":
                return bookRepository.findByTitleContainingIgnoreCaseOrderByTitle(query);
            case "author":
                return bookRepository.findByAuthorContainingIgnoreCaseOrderByAuthor(query);
            case "category":
                return bookRepository.findByCategory_CategoryNameContainingIgnoreCaseOrderByTitle(query);
            default:
                return bookRepository.findByTitleContainingIgnoreCaseOrderByTitle(query);
        }
    }

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



    // Existing methods...

    @Override
    public String uploadBookImage(MultipartFile image, String filename) {
        // Check if the image file is empty
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        try {
            // Generate a unique filename
            String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;

            // Define the path to save the uploaded image
            Path imagePath = Paths.get(uploadPath + File.separator + uniqueFilename);

            // Copy the image file to the target location
            Files.copy(image.getInputStream(), imagePath);

            // Return the URL or path of the uploaded image
            return imagePath.toString();
        } catch (IOException e) {
            // Handle the exception if file copying fails
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }


    }

}
