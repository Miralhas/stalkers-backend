package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.model.EldersChoice;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.EldersChoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EldersChoiceService {

	private final EldersChoiceRepository eldersChoiceRepository;
	private final NovelService novelService;

	@Transactional
	public void save(Novel novel) {
		EldersChoice eldersChoice = new EldersChoice(novel);
		eldersChoiceRepository.save(eldersChoice);
	}

	@Transactional
	public void delete(Long id) {
		eldersChoiceRepository.deleteById(id);
	}
}
