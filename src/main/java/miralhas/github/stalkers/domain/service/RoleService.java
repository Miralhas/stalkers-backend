package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.RoleNotFoundException;
import miralhas.github.stalkers.domain.model.auth.Role;
import miralhas.github.stalkers.domain.repository.RoleRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

	private final RoleRepository roleRepository;
	private final MessageSource messageSource;

	public Role getUserRole() {
		return roleRepository.findRoleByName(Role.Value.USER).orElseThrow(() -> new RoleNotFoundException(
				messageSource.getMessage("role.notFound", new Object[]{"USER"}, LocaleContextHolder.getLocale())
		));
	}

	public Role getAdminRole() {
		return roleRepository.findRoleByName(Role.Value.ADMIN).orElseThrow(() -> new RoleNotFoundException(
				messageSource.getMessage("role.notFound", new Object[]{"ADMIN"}, LocaleContextHolder.getLocale())
		));
	}

}
