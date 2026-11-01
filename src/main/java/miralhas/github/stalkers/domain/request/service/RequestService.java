package miralhas.github.stalkers.domain.request.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.shared.dto.PageDTO;
import miralhas.github.stalkers.api.request.dto.filter.RequestFilter;
import miralhas.github.stalkers.api.request.dto.input.FixChapterRequestInput;
import miralhas.github.stalkers.api.request.dto.input.NovelRequestInput;
import miralhas.github.stalkers.domain.request.interfaces.Request;
import miralhas.github.stalkers.api.request.mapper.RequestMapper;
import miralhas.github.stalkers.domain.shared.exception.BusinessException;
import miralhas.github.stalkers.domain.novel.exception.NovelNotFoundException;
import miralhas.github.stalkers.domain.user.model.User;
import miralhas.github.stalkers.domain.chapter.model.Chapter;
import miralhas.github.stalkers.domain.novel.model.Novel;
import miralhas.github.stalkers.domain.request.model.BaseRequest;
import miralhas.github.stalkers.domain.request.model.ChapterRequest;
import miralhas.github.stalkers.domain.request.model.FixChapterRequest;
import miralhas.github.stalkers.domain.request.repository.RequestRepository;
import miralhas.github.stalkers.shared.utils.ErrorMessages;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

	private final RequestRepository requestRepository;
	private final RequestMapper requestMapper;
	private final ErrorMessages errorMessages;
	private final ChapterErrorService chapterErrorService;

	@Cacheable(cacheNames = "requests.list", unless = "#result.results.empty")
	public PageDTO<Request> findAll(Pageable pageable, RequestFilter filter) {
		var pages = requestRepository.findAll(filter.toSpecification(), pageable);
		var dtos = pages.getContent().stream().map(requestMapper::toResponse).toList();
		var dtosPaged = new PageImpl<>(dtos, pageable, pages.getTotalElements());
		return new PageDTO<>(dtosPaged);
	}

	public BaseRequest findRequestByIdOrException(Long id) {
		return requestRepository.findById(id).orElseThrow(() -> new NovelNotFoundException(
				errorMessages.get("request.notFound", id)
		));
	}

	@Transactional
	@CacheEvict(cacheNames = "requests.list", allEntries = true)
	public Request createFixChapterRequest(FixChapterRequestInput input, Chapter chapter, User user) {
		var errors = input.errors()
				.stream().map(chapterErrorService::findBySlugOrException).collect(Collectors.toSet());

		var req = new FixChapterRequest();
		req.setChapter(chapter);
		req.setErrors(errors);
		req.setAnotherReason(input.anotherReason());
		req.setUser(user);

		req = requestRepository.save(req);
		return requestMapper.toResponse(req);
	}

	@Transactional
	@CacheEvict(cacheNames = "requests.list", allEntries = true)
	public Request createNovelRequest(NovelRequestInput input, User user) {
		var request = input.toNovelRequest();
		request.setUser(user);
		request = requestRepository.saveAndFlush(request);
		return requestMapper.toResponse(request);
	}

	@Transactional
	@CacheEvict(cacheNames = "requests.list", allEntries = true)
	public Request createChapterRequest(Novel novel, User user) {
		validateNovelRequest(novel);
		ChapterRequest request = new ChapterRequest();
		request.setUser(user);
		request.setNovel(novel);
		request = requestRepository.saveAndFlush(request);
		return requestMapper.toResponse(request);
	}

	@Transactional
	@CacheEvict(cacheNames = "requests.list", allEntries = true)
	public void complete(Long id) {
		var request = findRequestByIdOrException(id);
		request.complete();
		requestRepository.save(request);
	}

	@Transactional
	@CacheEvict(cacheNames = "requests.list", allEntries = true)
	public void deny(Long id) {
		var request = findRequestByIdOrException(id);
		request.deny();
		requestRepository.save(request);
	}

	private void validateNovelRequest(Novel novel) {
		if (novel.isCompleted()) throw new BusinessException(
				errorMessages.get("request.chapters.invalidStatus")
		);
	}
}
