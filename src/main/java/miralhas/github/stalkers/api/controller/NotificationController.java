package miralhas.github.stalkers.api.controller;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.service.NotificationService;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

	private final UserService userService;
	private final NotificationService notificationService;

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/recipients/{notificationId}")
	public void removeUserFromNotificationRecipients(
			JwtAuthenticationToken authToken, @PathVariable Long notificationId
	) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		notificationService.removeUserFromRecipients(user, notificationId);
	}

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/recipients/read")
	public void readAllUserUnreadNotifications() {
		notificationService.readAllUserUnreadNotifications();
	}
}
