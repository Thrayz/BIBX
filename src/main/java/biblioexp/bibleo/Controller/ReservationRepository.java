package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

}
