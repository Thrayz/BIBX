package biblioexp.bibleo.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ISBN;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "nbr_copies")
    private long nbr_copies;

    @Column(name = "avb_copies")
    private long avb_copies;

    @Column(name = "date_publication")
    private Date date_pub;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Loan> loans = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Notification> notifications = new HashSet<>();

    public Book() {
    }

    public Book(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Map the JSON string to a Book object
            Book book = objectMapper.readValue(jsonString, Book.class);

            // Copy the values from the mapped book to this instance
            this.setTitle(book.getTitle());
            this.setAuthor(book.getAuthor());
            this.setNbr_copies(book.getNbr_copies());
            this.setAvb_copies(book.getAvb_copies());
            this.setDate_pub(book.getDate_pub());
            this.setCategory(book.getCategory());

        } catch (IOException e) {
            // Handle the exception (e.g., log it or throw a specific exception)
            e.printStackTrace();
        }
    }
    public Book(String title, String author, long nbr_copies, long avb_copies, Date date_pub, Category category) {
        this.title = title;
        this.author = author;
        this.nbr_copies = nbr_copies;
        this.avb_copies = avb_copies;
        this.date_pub = date_pub;
        this.category = category;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getNbr_copies() {
        return nbr_copies;
    }

    public void setNbr_copies(long nbr_copies) {
        this.nbr_copies = nbr_copies;
    }

    public long getAvb_copies() {
        return avb_copies;
    }

    public void setAvb_copies(long avb_copies) {
        this.avb_copies = avb_copies;
    }

    public Date getDate_pub() {
        return date_pub;
    }

    public void setDate_pub(Date date_pub) {
        this.date_pub = date_pub;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    // ... other methods as needed

}