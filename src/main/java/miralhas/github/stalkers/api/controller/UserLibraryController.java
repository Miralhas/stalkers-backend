package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.UserLibraryDTO;
import miralhas.github.stalkers.api.dto.input.UserLibraryInput;
import miralhas.github.stalkers.domain.service.UserLibraryService;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class UserLibraryController {

	private final UserService userService;
	private final UserLibraryService userLibraryService;

	@GetMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	public List<UserLibraryDTO> findUserLibrary(JwtAuthenticationToken authToken) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		return userLibraryService.findUserLibrary(user);
	}


	@PutMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	public UserLibraryDTO updateUserLibrary(
			@RequestBody @Valid UserLibraryInput userHistoryInput,
			JwtAuthenticationToken authToken
	) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		return userLibraryService.updateUserLibrary(user, userHistoryInput.novelId(), userHistoryInput.chapterId());
	}
}
