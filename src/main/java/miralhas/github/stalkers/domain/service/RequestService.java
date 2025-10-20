package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.NovelRequestInput;
import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;
import miralhas.github.stalkers.api.dto_mapper.RequestMapper;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.model.requests.ChapterRequest;
import miralhas.github.stalkers.domain.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

	private final RequestRepository requestRepository;
	private final RequestMapper requestMapper;

	public List<RequestDTO> findAll() {
		return requestRepository.findAll().stream().map(requestMapper::toResponse).toList();
	}

	@Transactional
	public void createNovelRequest(NovelRequestInput input, User user) {
		var request = input.toNovelRequest();
		request.setUser(user);
		requestRepository.save(request);
	}

	@Transactional
	public void createChapterRequest(Novel novel, User user) {
		ChapterRequest request = new ChapterRequest();
		request.setUser(user);
		request.setNovel(novel);
		requestRepository.save(request);
	}

	public void complete() {

	}

	public void deny() {

	}
}
