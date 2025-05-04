package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.UserLibraryDTO;
import miralhas.github.stalkers.api.dto.filter.LibraryFilter;
import miralhas.github.stalkers.api.dto.input.UserLibraryInput;
import miralhas.github.stalkers.domain.repository.UserLibraryRepository;
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

import java.util.Map;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class UserLibraryController {

	private final UserService userService;
	private final UserLibraryService userLibraryService;
	private final NovelService novelService;
	private final UserLibraryRepository userLibraryRepository;

	@GetMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	public PageDTO<UserLibraryDTO> findUserLibrary(
			@PageableDefault(size = 20, sort = {"lastReadAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
			LibraryFilter filter,
			JwtAuthenticationToken authToken
	) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		Page<UserLibraryDTO> userLibraryDTOPage = userLibraryService.findUserLibrary(user, filter, pageable);
		return new PageDTO<>(userLibraryDTOPage);
	}


	@PutMapping
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateUserLibrary(
			@RequestBody @Valid UserLibraryInput userLibraryInput,
			JwtAuthenticationToken authToken
	) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		userLibraryService.updateUserLibrary(user, userLibraryInput.novelId(), userLibraryInput.chapterId());
	}

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/bookmark/{novelSlug}")
	public void bookmarkNovel(@PathVariable String novelSlug, JwtAuthenticationToken authToken) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		var novel = novelService.findBySlugOrException(novelSlug);
		userLibraryService.bookmarkNovel(user, novel);
	}

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/bookmark/{libraryElementId}")
	public void removeBookmark(@PathVariable Long libraryElementId) {
		userLibraryService.removeBookmark(libraryElementId);
	}

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/complete/{novelSlug}")
	public void completeNovel(@PathVariable String novelSlug, JwtAuthenticationToken authToken) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		var novel = novelService.findBySlugOrException(novelSlug);
		userLibraryService.libraryNovelCompleted(user, novel);
	}

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/complete/{libraryElementId}")
	public void removeComplete(@PathVariable Long libraryElementId) {
		userLibraryService.removeComplete(libraryElementId);
	}

	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/bookmark/is-novel-bookmarked/{novelSlug}")
	public Map<String, Boolean> isNovelBookmarkedByUser(@PathVariable String novelSlug, JwtAuthenticationToken authToken) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		var isBookmarked = userLibraryRepository.isNovelBookmarkedByUser(novelSlug, user.getId());
		return Map.of("isBookmarked", isBookmarked);
	}
}
