package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.EldersChoiceDTO;
import miralhas.github.stalkers.api.dto_mapper.EldersChoiceMapper;
import miralhas.github.stalkers.domain.exception.NovelAlreadySelectedException;
import miralhas.github.stalkers.domain.model.EldersChoice;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.EldersChoiceRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "elders-choice")
public class EldersChoiceService {

	private final EldersChoiceRepository eldersChoiceRepository;
	private final EldersChoiceMapper eldersChoiceMapper;
	private final ErrorMessages errorMessages;

	@Cacheable(unless = "#result.isEmpty()")
	public List<EldersChoiceDTO> getEldersChoiceDTO() {
		return eldersChoiceRepository.findAll()
				.stream()
				.map(eldersChoiceMapper::toResponse)
				.toList();
	}

	@Transactional
	@CacheEvict(cacheNames = "elders-choice", allEntries = true)
	public void save(Novel novel) {
		validate(novel.getId());
		EldersChoice eldersChoice = new EldersChoice(novel);
		eldersChoiceRepository.save(eldersChoice);
	}

	private void validate(Long novelId) {
		if (eldersChoiceRepository.novelAlreadySelected(novelId)) {
			throw new NovelAlreadySelectedException(errorMessages.get("elders-choice.alreadySelected", novelId));
		}
	}

	@Transactional
	@CacheEvict(cacheNames = "elders-choice", allEntries = true)
	public void delete(Long id) {
		eldersChoiceRepository.deleteById(id);
	}
}
