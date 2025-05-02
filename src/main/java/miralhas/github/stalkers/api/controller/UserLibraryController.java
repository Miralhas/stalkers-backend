package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.UserLibraryDTO;
import miralhas.github.stalkers.api.dto.input.UserLibraryInput;
import miralhas.github.stalkers.domain.service.NovelService;
import miralhas.github.stalkers.domain.service.UserLibraryService;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class UserLibraryController {

	private final UserService userService;
	private final UserLibraryService userLibraryService;
	private final NovelService novelService;

	@GetMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	public PageDTO<UserLibraryDTO> findUserLibrary(
			@RequestParam(name = "bookmarked", defaultValue = "false", required = false) Boolean bookmarked,
			@PageableDefault(size = 20, sort = {"lastReadAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
			JwtAuthenticationToken authToken
	) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		Page<UserLibraryDTO> userLibraryDTOPage = userLibraryService.findUserLibrary(user, bookmarked, pageable);
		return new PageDTO<>(userLibraryDTOPage);
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

	@PutMapping("/bookmark/{novelSlug}")
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	public void bookmarkNovel(@PathVariable String novelSlug, JwtAuthenticationToken authToken) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		var novel = novelService.findBySlugOrException(novelSlug);
		userLibraryService.bookmarkNovel(user, novel);
	}
}
