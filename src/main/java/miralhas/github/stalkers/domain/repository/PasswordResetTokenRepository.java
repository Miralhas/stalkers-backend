package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.auth.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	@Query("from PasswordResetToken prt WHERE prt.token = :token")
	Optional<PasswordResetToken> findByToken(@Param("token") String token);

}