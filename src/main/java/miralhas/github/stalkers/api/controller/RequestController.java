package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.filter.RequestFilter;
import miralhas.github.stalkers.api.dto.input.NovelRequestInput;
import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;
import miralhas.github.stalkers.domain.service.NovelService;
import miralhas.github.stalkers.domain.service.RequestService;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requests")
public class RequestController {

	private final UserService userService;
	private final RequestService requestService;
	private final NovelService novelService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public PageDTO<RequestDTO> findAllRequests(
			@PageableDefault(size = 100, sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@Valid RequestFilter filter
			) {
		return requestService.findAll(pageable, filter);
	}

	@PostMapping("/novels")
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public void createNovelRequest(
			@RequestBody @Valid NovelRequestInput input,
			JwtAuthenticationToken authToken
	) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		requestService.createNovelRequest(input, user);
	}

	@PostMapping("/novels/{novelSlug}")
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public void createChapterRequest(
			JwtAuthenticationToken authToken,
			@PathVariable String novelSlug
	) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		var novel = novelService.findBySlugOrException(novelSlug);
		requestService.createChapterRequest(novel, user);
	}

	@PutMapping("/{id}/complete")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void completeRequest(@PathVariable Long id) {
		requestService.complete(id);
	}

	@PutMapping("/{id}/deny")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void denyRequest(@PathVariable Long id) {
		requestService.deny(id);
	}

}
