package km.EH.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    @Query("SELECT u from User u where u.username = ?1 or u.email = ?1")
    Optional<User> findUserByUsernameOrEmail(String data);
}
