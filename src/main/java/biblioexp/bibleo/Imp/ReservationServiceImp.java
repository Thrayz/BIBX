package biblioexp.bibleo.Imp;

import biblioexp.bibleo.Controller.ReservationRepository;
import biblioexp.bibleo.Entity.Category;
import biblioexp.bibleo.Entity.Reservation;
import biblioexp.bibleo.Service.ReservationService;
import biblioexp.bibleo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImp implements ReservationService {
    private ReservationRepository ReservationRepository;
    public ReservationServiceImp (ReservationRepository ReservationRepository){
        super();
        this.ReservationRepository= ReservationRepository;
    }
    @Override
    public Reservation saveReservation(Reservation Reservation) {
        return ReservationRepository.save(Reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return ReservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(long id) {
        return ReservationRepository.findById(id).orElseThrow(() ->
                new
                        ResourceNotFoundException("Reservation", "Id", id));
    }

    @Override
    public Reservation updateReservation(Reservation Reservation, long id) {
        Reservation existingReservation =
                ReservationRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Reservation",
                                "Id", id));
        existingReservation.setUser(existingReservation.getUser());
        existingReservation.setBook(existingReservation.getBook());
        existingReservation.setReservationDate(existingReservation.getReservationDate());
        ReservationRepository.save(existingReservation);
        return existingReservation;
    }

    @Override
    public void deleteReservation(long id) {

       ReservationRepository.findById(id).orElseThrow(() ->
                new
                        ResourceNotFoundException("Reservation", "Id", id));
        ReservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> findReservedBooksAvailable() {
        // Find reservations where the reserved book is available (loan not taken)
        return ReservationRepository.findReservedBooksAvailable();
    }
}
