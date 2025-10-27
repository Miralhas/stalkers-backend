package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.ChapterErrorInput;
import miralhas.github.stalkers.domain.exception.ChapterErrorAlreadyExistsException;
import miralhas.github.stalkers.domain.exception.ChapterErrorNotFoundException;
import miralhas.github.stalkers.domain.model.requests.ChapterError;
import miralhas.github.stalkers.domain.repository.ChapterErrorRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChapterErrorService {

	private final ChapterErrorRepository chapterErrorRepository;
	private final ErrorMessages errorMessages;

	public ChapterError findBySlugOrException(String slug) {
		return chapterErrorRepository.findBySlug(slug).orElseThrow(() -> new ChapterErrorNotFoundException(
				errorMessages.get("chapterError.notFound", slug)
		));
	}

	@Transactional
	public void create(ChapterErrorInput input) {
		var error = new ChapterError();
		error.setName(input.name());
		error.setDescription(input.description());

		error.generateSlug();
		validateSlug(error.getSlug());

		chapterErrorRepository.save(error);
	}

	private void validateSlug(String slug) {
		if (chapterErrorRepository.checkIfSlugAlreadyExists(slug)) {
			throw new ChapterErrorAlreadyExistsException(
					errorMessages.get("chapterError.alreadyExists.slug", slug)
			);
		}
	}

}
