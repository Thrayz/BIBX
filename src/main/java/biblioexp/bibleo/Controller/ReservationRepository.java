package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findByUserId(Long userId);

    @Query("SELECT r FROM Reservation r WHERE r.book.avb_copies > 0")
    List<Reservation> findReservedBooksAvailable();

}
