package biblioexp.bibleo.util;

import biblioexp.bibleo.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    @Override
    public void run(String... args) throws Exception {
        //String csvFilePath = "C:/Users/USER/Desktop/BIBLIOEXP/src/main/resources/static/file.csv";
        //bookService.loadBooksFromCSV(csvFilePath);
    }
}
