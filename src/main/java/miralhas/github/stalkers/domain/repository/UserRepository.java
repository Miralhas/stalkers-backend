package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("from User u where u.email = :email")
	Optional<User> findUserByEmail(String email);

	@Query("from User u where u.username = :username")
	Optional<User> findUserByUsername(String username);

}