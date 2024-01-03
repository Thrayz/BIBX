package biblioexp.bibleo.Service;

import biblioexp.bibleo.Entity.Category;
import biblioexp.bibleo.Entity.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ReservationService {
    Reservation saveReservation(Reservation Reservation);
    List<Reservation> getAllReservations();
    Reservation getReservationById(long id);
    Reservation updateReservation(Reservation Reservation, long id);
    void deleteReservation(long id);
    List<Reservation> findReservedBooksAvailable();


}
