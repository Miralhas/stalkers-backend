package miralhas.github.stalkers.domain.auth.repository;

import miralhas.github.stalkers.domain.auth.model.RefreshToken;
import miralhas.github.stalkers.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

	@Modifying
	@Query("delete from RefreshToken rt where rt.user = :user")
	void deleteAllUserRefreshTokens(User user);

}