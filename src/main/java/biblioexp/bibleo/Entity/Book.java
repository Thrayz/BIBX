package biblioexp.bibleo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
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

    @Column(name = "loaned_copies")
    private Long loanedCopies;

    @Column(name = "reserved_copies")
    private Long reservedCopies;

    @Column(name = "date_publication")
    private String date_pub;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = true)
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Loan> loans = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Notification> notifications = new HashSet<>();

    public Book() {
    }

    public Book(String title, String author, long nbr_copies, long avb_copies, String date_pub, Category category) {
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

    public Long getLoanedCopies() {
        return loanedCopies;
    }

    public void setLoanedCopies(long loanedCopies) {
        this.loanedCopies = loanedCopies;
    }

    public Long getReservedCopies() {
        return reservedCopies;
    }

    public void setReservedCopies(long reservedCopies) {
        this.reservedCopies = reservedCopies;
    }

    public String getDate_pub() {
        return date_pub;
    }

    public void setDate_pub(String date_pub) {
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
}
