package biblioexp.bibleo.Controller;

import biblioexp.bibleo.Entity.Loan;
import biblioexp.bibleo.Entity.Notification;
import biblioexp.bibleo.Entity.Reservation;
import biblioexp.bibleo.Entity.User;
import biblioexp.bibleo.Imp.LoanServiceImp;
import biblioexp.bibleo.Service.LoanService;
import biblioexp.bibleo.Service.NotificationService;
import biblioexp.bibleo.Service.UserService;

import biblioexp.bibleo.config.CustomLoginSucessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;

    private final LoanService loanService;

    public UserController(UserService userService, NotificationService notificationService, LoanService loanService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.loanService = loanService;
    }

    // Create User REST API
    @PostMapping()
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Get all Users REST API
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get User by id REST API
    // http://localhost:8080/api/users/1
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update User REST API
    // http://localhost:8080/api/users/1
    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        ModelAndView modelAndView = new ModelAndView("update-user");
        modelAndView.addObject("user", user);
        return modelAndView;
    }




    @PostMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable("id") long id, @Valid User user,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return new ModelAndView("redirect:/api/users/update/{id}");
        }


        userService.updateUser(user, id);
        return new ModelAndView("redirect:/api/users/userList");
    }
    // Delete User REST API
    // http://localhost:8080/api/users/1
    @GetMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") long id, Model model) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/api/users/userList");
    }

    @GetMapping("/userList")
    public ModelAndView showUserList(HttpServletResponse response) {
        List<User> userList = userService.getAllUsers();


        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        ModelAndView modelAndView = new ModelAndView("userList");
        modelAndView.addObject("userList", userList);

        return modelAndView;
    }

    @RequestMapping(value = {"/dashboard"}, method = RequestMethod.GET)
    public String homePage(){
        return "user/dashboard";
    }

    @GetMapping("/loans")
    public ModelAndView getUserLoans(HttpServletRequest request) {
        Long userIdFromCookie = CustomLoginSucessHandler.getUserIDFromCookie(request);
        User user = userService.getUserWithLoans(userIdFromCookie);
        if (user != null) {
            ModelAndView modelAndView = new ModelAndView("userLoanList");
            modelAndView.addObject("loanList", user.getLoans());
            return modelAndView;
        } else {
            return new ModelAndView("error404");
        }
    }




    @GetMapping("/reservations")
    public ModelAndView getUserReservations(HttpServletRequest request) {
        Long userIdFromCookie = CustomLoginSucessHandler.getUserIDFromCookie(request);
        User user = userService.getUserWithReservations(userIdFromCookie);
        if (user != null) {
            ModelAndView modelAndView = new ModelAndView("userReservationList");
            modelAndView.addObject("reservationList", user.getReservations());
            return modelAndView;
        } else {
            return new ModelAndView("error404");
        }
    }


    @GetMapping("/notifications")
    public ModelAndView getUserNotifications(HttpServletRequest request) {
        Long userIdFromCookie = CustomLoginSucessHandler.getUserIDFromCookie(request);
        User user = userService.getUserWithNotifications(userIdFromCookie);
        if (user != null) {
            Set<Notification> notifications = user.getNotifications();


            for (Notification notification : notifications) {
                notification.setViewed(true);
                notificationService.sendNotification(notification);
            }

            ModelAndView modelAndView = new ModelAndView("userNotificationList");
            modelAndView.addObject("notificationList", notifications);
            return modelAndView;
        } else {
            return new ModelAndView("error404");
        }
    }

    @PostMapping("/loans/renew")
    public ModelAndView renewLoan(@RequestParam("loanId") long loanId) {
        Loan renewedLoan = loanService.renewLoan(loanId);

        return new ModelAndView("redirect:/api/users/loans");
    }

    @PostMapping("/loans/return")
    public ModelAndView returnLoan(@RequestParam("loanId") long loanId) {
        Loan returnedLoan = loanService.returnLoan(loanId);

        return new ModelAndView("redirect:/api/users/loans");
    }


}
