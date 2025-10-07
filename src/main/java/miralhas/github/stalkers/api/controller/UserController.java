package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.*;
import miralhas.github.stalkers.api.dto.input.ImageInput;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import miralhas.github.stalkers.api.dto.interfaces.NotificationDTO;
import miralhas.github.stalkers.api.dto_mapper.ImageMapper;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.repository.NotificationRepository;
import miralhas.github.stalkers.domain.repository.RatingRepository;
import miralhas.github.stalkers.domain.repository.UserRepository;
import miralhas.github.stalkers.domain.service.ImageService;
import miralhas.github.stalkers.domain.service.MetricsService;
import miralhas.github.stalkers.domain.service.NotificationService;
import miralhas.github.stalkers.domain.service.UserService;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
public class UserController  {

	private final UserMapper userMapper;
	private final UserService userService;
	private final ImageMapper imageMapper;
	private final ImageService imageService;
	private final NotificationService notificationService;
	private final ValidateAuthorization validateAuthorization;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;
	private final RatingRepository ratingRepository;
	private final MetricsService metricsService;

//	@Override
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public PageDTO<UserInfoDTO> getAllUsers(
			@PageableDefault(direction = Sort.Direction.ASC) Pageable pageable
	) {
		var users = userRepository.findAllUserInfo(pageable);
		var x = users.getContent().stream().map(UserInfoProjection::getUserInfoDTO).toList();
		var pg = new PageImpl<>(x, pageable, users.getTotalElements());
		return new PageDTO<>(pg);
	}

	@GetMapping("/info")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	public UserInfoDTO getUserInfo(JwtAuthenticationToken authToken) {
		return userService.getUserInfoOrException(authToken.getName());
	}

	@GetMapping("/validate")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	public UserDTO verifyUserAccessToken(JwtAuthenticationToken authToken) {
		User user = userService.findUserByEmailOrException(authToken.getName());
		return userMapper.toResponse(user);
	}

//	@Override
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

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ROBOT')")
	public void deleteUser(@PathVariable Long id) {
		var user = userService.findUserByIdOrException(id);
		userService.deleteUser(user);
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
	public PageDTO<UserChapterCommentDTO> getUserChapterComments(
			@PageableDefault(size = 10, sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		var currentUser = validateAuthorization.getCurrentUser();
		Page<UserChapterCommentDTO> comments = userRepository.findAllUserChapterComments(currentUser.getId(), pageable);
		return new PageDTO<>(comments);
	}

	@GetMapping("/reviews")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	public PageDTO<UserReviewDTO> getUserNovelReviews(
			@PageableDefault(size = 10, sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		var currentUser = validateAuthorization.getCurrentUser();
		var comments =  userRepository.findAllUserNovelReviews(currentUser.getId(), pageable);
		return new PageDTO<>(comments);
	}

	@GetMapping("/ratings/{novelId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('USER')")
	public UserRatingDTO getUserRatingOnNovel(@PathVariable Long novelId, JwtAuthenticationToken token) {
		var user = userService.findUserByEmailOrException(token.getName());
		return metricsService.getUserRatingOnNovel(user.getId(), novelId);
	}

}
