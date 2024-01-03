package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Category;
import biblioexp.bibleo.Entity.Reservation;
import biblioexp.bibleo.Service.CategoryService;
import biblioexp.bibleo.Service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Reservations")
public class ReservationController {
    private ReservationService ReservationService;
    public ReservationController(ReservationService reservationService) {
        super();
        this.ReservationService =reservationService;
    }
    @PostMapping()
    public ResponseEntity<Reservation> saveReservation(@RequestBody Reservation Reservation){
        return new
                ResponseEntity<Reservation>(ReservationService.saveReservation(Reservation),
                HttpStatus.CREATED);
    }

    @GetMapping()
    public List<Reservation> getAllReservations(){return ReservationService.getAllReservations();
        }


    @GetMapping("id")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id")
                                                    long id){
        return new
                ResponseEntity<Reservation>(ReservationService.getReservationById(id),
                HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id")
                                                   long id
            ,@RequestBody Reservation Reservation){
        return new
                ResponseEntity<Reservation>(ReservationService.updateReservation(Reservation, id),HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable("id") long
                                                         id){
        ReservationService.deleteReservation(id);
        return new ResponseEntity<String>("Reservation deleted successfully!.", HttpStatus.OK);
    }



}