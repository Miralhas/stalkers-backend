package miralhas.github.stalkers.shared.utils;

import miralhas.github.stalkers.domain.user.model.User;
import miralhas.github.stalkers.domain.user.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ValidateAuthorization {

	private final UserService userService;
	private final ErrorMessages errorMessages;

	public ValidateAuthorization(@Lazy UserService userService, ErrorMessages errorMessages) {
		this.userService = userService;
		this.errorMessages = errorMessages;
	}

	public User getCurrentUser() {
		return  userService.findUserByEmailOrException(
				SecurityContextHolder.getContext().getAuthentication().getName()
		);
	}

	public void validate(User imageOwner) {
		var currentUser = getCurrentUser();
		if (currentUser.isAdmin() || currentUser.equals(imageOwner)) return;
		throw new AccessDeniedException(errorMessages.get("AbstractAccessDecisionManager.accessDenied"));
	}
}
