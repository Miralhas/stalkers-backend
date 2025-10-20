package miralhas.github.stalkers.api.dto_mapper;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.ChapterRequestDTO;
import miralhas.github.stalkers.api.dto.NovelRequestDTO;
import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;
import miralhas.github.stalkers.domain.exception.InternalServerError;
import miralhas.github.stalkers.domain.model.requests.BaseRequest;
import miralhas.github.stalkers.domain.model.requests.ChapterRequest;
import miralhas.github.stalkers.domain.model.requests.NovelRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RequestMapper {

	private final NovelMapper novelMapper;
	private final UserMapper userMapper;

	public RequestDTO toResponse(BaseRequest request) {
		return switch (request) {
			case ChapterRequest chapterRequest -> mapToChapterRequest(chapterRequest);
			case NovelRequest novelRequest -> mapToNovelRequest(novelRequest);
			case null, default ->
					throw new InternalServerError(
							"Unsupported Request type: " + Objects.requireNonNull(request).getClass().getName()
					);

		};
	}

	private ChapterRequestDTO mapToChapterRequest(ChapterRequest chapterRequest) {
		var novelInfoDTO = novelMapper.toInfoResponse(chapterRequest.getNovel());
		var userDTO = userMapper.toResponse(chapterRequest.getUser());
		return new ChapterRequestDTO(
				chapterRequest.getId(),
				chapterRequest.getCreatedAt(),
				novelInfoDTO,
				chapterRequest.getRequestType(),
				userDTO,
				chapterRequest.getStatus().name()
		);
	}

	private NovelRequestDTO mapToNovelRequest(NovelRequest novelRequest) {
		var userDTO = userMapper.toResponse(novelRequest.getUser());
		return new NovelRequestDTO(
				novelRequest.getId(),
				novelRequest.getCreatedAt(),
				novelRequest.getNovelTitle(),
				novelRequest.getRequestType(),
				novelRequest.getStatus().name(),
				userDTO
		);
	}
}
