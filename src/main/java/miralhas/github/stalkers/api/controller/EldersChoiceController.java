package miralhas.github.stalkers.api.controller;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.EldersChoiceDTO;
import miralhas.github.stalkers.domain.service.EldersChoiceService;
import miralhas.github.stalkers.domain.service.NovelService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elders-choice")
public class EldersChoiceController {

	private final EldersChoiceService eldersChoiceService;
	private final NovelService novelService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<EldersChoiceDTO> getEldersChoice() {
		return eldersChoiceService.getEldersChoiceDTO();
	}

	@PostMapping("/{novelSlug}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eldersChoice(@PathVariable String novelSlug) {
		eldersChoiceService.save(novelService.findBySlugOrException(novelSlug));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteChoice(@PathVariable Long id) {
		eldersChoiceService.delete(id);
	}
}
