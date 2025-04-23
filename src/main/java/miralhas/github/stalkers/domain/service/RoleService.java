package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.RoleNotFoundException;
import miralhas.github.stalkers.domain.model.auth.Role;
import miralhas.github.stalkers.domain.repository.RoleRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

	private final RoleRepository roleRepository;
	private final ErrorMessages errorMessages;

	public Role getUserRole() {
		return roleRepository.findRoleByName(Role.Value.USER).orElseThrow(() -> new RoleNotFoundException(
				errorMessages.get("role.notFound", "USER")
		));
	}

	public Role getAdminRole() {
		return roleRepository.findRoleByName(Role.Value.ADMIN).orElseThrow(() -> new RoleNotFoundException(
				errorMessages.get("role.notFound", "ADMIN")
		));
	}
}
