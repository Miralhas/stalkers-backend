package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query("from Role r where r.name = :name")
	Optional<Role> findRoleByName(Role.Value name);

}
