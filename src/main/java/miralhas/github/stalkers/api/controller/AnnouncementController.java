package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.AnnouncementDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.input.AnnouncementInput;
import miralhas.github.stalkers.api.dto.input.UpdateAnnouncementInput;
import miralhas.github.stalkers.domain.service.AnnouncementService;
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
@RequestMapping("/api/announcements")
public class AnnouncementController {

	private final UserService userService;
	private final AnnouncementService announcementService;

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public PageDTO<AnnouncementDTO> findAllAnnouncements(
			@PageableDefault(sort = {"pinned", "createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		return announcementService.findAll(pageable);
	}

	@GetMapping("/{slug}")
	@ResponseStatus(HttpStatus.OK)
	public AnnouncementDTO findAllAnnouncements(@PathVariable String slug) {
		return announcementService.findDTOBySlugCacheable(slug);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public AnnouncementDTO createAnnouncement(@RequestBody @Valid AnnouncementInput input, JwtAuthenticationToken authToken) {
		var user = userService.findUserByEmailOrException(authToken.getName());
		return announcementService.create(input, user);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public AnnouncementDTO updateAnnouncement(
			@RequestBody @Valid UpdateAnnouncementInput input, @PathVariable Long id
	) {
		return announcementService.update(input, id);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAnnouncement(@PathVariable Long id) {
		announcementService.delete(id);
	}
}
