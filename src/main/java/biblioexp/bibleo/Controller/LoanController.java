package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Loan;
import biblioexp.bibleo.Service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Loans")
public class LoanController {
    private LoanService LoanService;
    public LoanController(LoanService loanService){
        super();
        this.LoanService=loanService;
    }
    /*//create Loan
    @PostMapping()
    public ResponseEntity<Loan>saveLoan(@RequestBody Loan Loan){
        return new ResponseEntity<Loan>(LoanService.saveLoan(Loan), HttpStatus.CREATED);
    }
    // get all loans
    @GetMapping()
    public List<Loan> getAllLoans(){
        return LoanService.getAllLoans();
    }
    @GetMapping("id")
    public ResponseEntity<Loan> getLoanById(@PathVariable("id") long id ){
        return  new ResponseEntity<Loan>(LoanService.getLoanById(id),HttpStatus.OK);
    }
    @PutMapping("id")
    public ResponseEntity<Loan> UpdateLoan(@PathVariable("id") long id , @RequestBody Loan Loan){
        return  new ResponseEntity<Loan>(LoanService.UpdateLoan(Loan ,id),HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> DeleteLoan(@PathVariable("id") long id){
        LoanService.DeleteLoan(id);
        return new ResponseEntity<String>("Loan deleted successfully!.", HttpStatus.OK);
    }
*/
}
