package miralhas.github.stalkers.domain.novel.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.novel.exception.TagNotFoundException;
import miralhas.github.stalkers.domain.novel.model.Tag;
import miralhas.github.stalkers.domain.novel.repository.TagRepository;
import miralhas.github.stalkers.shared.utils.ErrorMessages;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagsService {

	private final TagRepository tagRepository;
	private final ErrorMessages errorMessages;

	@Cacheable("tags.detail")
	public Tag findTagByNameOrException(String name){
		return tagRepository.findByName(name).orElseThrow(() -> new TagNotFoundException(
				errorMessages.get("tag.notFound.name", name)
		));
	}

	@Cacheable("tags.detail")
	public Tag findTagBySlugOrException(String slug){
		return tagRepository.findBySlug(slug).orElseThrow(() -> new TagNotFoundException(
				errorMessages.get("tag.notFound.slug", slug)
		));
	}

	public boolean tagExists(String name) {
		return tagRepository.findByName(name).isPresent();
	}

}
