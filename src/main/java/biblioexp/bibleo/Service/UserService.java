package biblioexp.bibleo.Service;

import biblioexp.bibleo.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(User user);
    List<User> getAllUsers();
    void deleteUser(Long userId);
    User getUserById(Long userId);
    User updateUser(User user, Long userId);
    List<Object> isUserPresent(User user);
    Optional<User> findById(Long userId);

    User getUserWithLoans(Long userId);

    User getUserWithReservations(Long userId);
    User getUserWithNotifications(long userId);
}
