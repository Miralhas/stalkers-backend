package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.AnnouncementDTO;
import miralhas.github.stalkers.api.dto.PageDTO;
import miralhas.github.stalkers.api.dto.input.AnnouncementInput;
import miralhas.github.stalkers.api.dto.input.UpdateAnnouncementInput;
import miralhas.github.stalkers.api.dto_mapper.AnnouncementMapper;
import miralhas.github.stalkers.domain.exception.AnnouncementAlreadyExistsException;
import miralhas.github.stalkers.domain.exception.AnnouncementNotFoundException;
import miralhas.github.stalkers.domain.model.Announcement;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.repository.AnnouncementRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementService {

	private final AnnouncementMapper announcementMapper;
	private final AnnouncementRepository announcementRepository;
	private final ErrorMessages errorMessages;

	public PageDTO<AnnouncementDTO> findAll() {
		return null;
	}

	public Announcement findBySlugOrException(String slug) {
		return announcementRepository.findBySlug(slug).orElseThrow(() -> new AnnouncementNotFoundException(
				errorMessages.get("announcement.notFound.slug", slug))
		);
	}

	public Announcement findByIdOrException(Long id) {
		return announcementRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(
				errorMessages.get("announcement.notFound.id", id))
		);
	}

	@Transactional
	public AnnouncementDTO create(AnnouncementInput input, User user) {
		var announcement = announcementMapper.fromInput(input);
		announcement.setUser(user);

		announcement.generateSlug();
		validateSlug(announcement.getSlug());

		announcement = announcementRepository.save(announcement);
		return announcementMapper.toResponse(announcement);
	}

	@Transactional
	public AnnouncementDTO update(UpdateAnnouncementInput input, Long id) {
		var announcement = findByIdOrException(id);
		var initialTitle = announcement.getTitle();

		announcement = announcementMapper.update(input, announcement);
		announcement.generateSlug();

		// if updated title is different from previous title, then perform a slug validation.
		boolean shouldValidateSlug = !initialTitle.equals(announcement.getTitle());
		if (shouldValidateSlug) validateSlug(announcement.getSlug());

		announcement = announcementRepository.save(announcement);

		return announcementMapper.toResponse(announcement);
	}

	@Transactional
	public void delete(Long id) {
		announcementRepository.deleteById(id);
	}

	private void validateSlug(String slug) {
		var exists = announcementRepository.checkIfSlugAlreadyExists(slug);
		if (exists) throw new AnnouncementAlreadyExistsException(
				errorMessages.get("announcement.alreadyExists.slug", slug)
		);
	}


}
