package biblioexp.bibleo.Controller;


import biblioexp.bibleo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMobile(String mobile);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.loans WHERE u.id = :userId")
    Optional<User> findByIdWithLoans(@Param("userId") Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.reservations WHERE u.id = :userId")
    Optional<User> findByIdWithReservations(@Param("userId") Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.notifications WHERE u.id = :userId")
    Optional<User> findByIdWithNotifications(@Param("userId") long userId);
}

