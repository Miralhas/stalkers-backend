package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.TagNotFoundException;
import miralhas.github.stalkers.domain.model.novel.Tag;
import miralhas.github.stalkers.domain.repository.TagRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
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

}
