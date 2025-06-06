package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.ImageDTO;
import miralhas.github.stalkers.api.dto.UserCommentDTO;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.input.ImageInput;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.api.dto_mapper.ImageMapper;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.api.swagger.UserControllerSwagger;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.repository.NotificationRepository;
import miralhas.github.stalkers.domain.repository.UserRepository;
import miralhas.github.stalkers.domain.service.ImageService;
import miralhas.github.stalkers.domain.service.NotificationService;
import miralhas.github.stalkers.domain.service.UserService;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerSwagger {

	private final UserMapper userMapper;
	private final UserService userService;
	private final ImageMapper imageMapper;
	private final ImageService imageService;
	private final NotificationService notificationService;
	private final ValidateAuthorization validateAuthorization;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;

	@Override
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public List<UserDTO> getAllUsers() {
		var users = userService.findAll();
		return userMapper.toCollectionResponse(users);
	}

	@GetMapping("/validate")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	public UserDTO verifyUserAccessToken(JwtAuthenticationToken authToken) {
		User user = userService.findUserByEmailOrException(authToken.getName());
		return userMapper.toResponse(user);
	}

	@Override
	@PatchMapping
	@ResponseStatus(HttpStatus.OK)
	public UserDTO updateUser(@RequestBody @Valid UpdateUserInput updateUserInput, JwtAuthenticationToken token) {
		var user = userService.findUserByEmailOrException(token.getName());
		user = userService.update(updateUserInput, user);
		return userMapper.toResponse(user);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}/image")
	public ResponseEntity<InputStreamResource> getUserImage(
			@PathVariable Long id, @RequestHeader(name = "accept", defaultValue = "image/*") String acceptHeader
	) {
		var user = userService.findUserByIdOrException(id);
		return imageService.getImage(user.getImage());
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ImageDTO saveUserImage(@PathVariable Long id, @Valid ImageInput imageInput) throws IOException {
		var image = imageMapper.fromInput(imageInput);
		var user = userService.findUserByIdOrException(id);
		image = userService.saveUserImage(user, image, imageInput.fileInputStream());
		return imageMapper.toResponse(image);
	}

	@DeleteMapping("/{id}/image")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUserImage(@PathVariable Long id) {
		var user = userService.findUserByIdOrException(id);
		userService.deleteUserImage(user);
	}

	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/notifications")
	public List<NotificationDTO> getUserNotifications(JwtAuthenticationToken token) {
		var user = userService.findUserByEmailOrException(token.getName());
		return notificationService.getUserNotifications(user);
	}

	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/notifications/unread-count")
	public Map<String, Long> getUserUnreadNotificationsCount(JwtAuthenticationToken token) {
		var user = userService.findUserByEmailOrException(token.getName());
		Long count = notificationRepository.findUserUnreadNotificationsCount(user.getId());
		return Map.of("count", count);
	}

	@GetMapping("/comments")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	public List<UserCommentDTO> getUserChapterComments() {
		var currentUser = validateAuthorization.getCurrentUser();
		return userRepository.findAllUserChapterComments(currentUser.getId());
	}

	@GetMapping("/reviews")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	public List<UserCommentDTO> getUserNovelReviews() {
		var currentUser = validateAuthorization.getCurrentUser();
		return userRepository.findAllUserNovelReviews(currentUser.getId());
	}


}
