package miralhas.github.stalkers.api.dto_mapper;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.ChapterRequestDTO;
import miralhas.github.stalkers.api.dto.FixChapterRequestDTO;
import miralhas.github.stalkers.api.dto.NovelRequestDTO;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.interfaces.RequestDTO;
import miralhas.github.stalkers.domain.exception.InternalServerError;
import miralhas.github.stalkers.domain.model.requests.BaseRequest;
import miralhas.github.stalkers.domain.model.requests.ChapterRequest;
import miralhas.github.stalkers.domain.model.requests.FixChapterRequest;
import miralhas.github.stalkers.domain.model.requests.NovelRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RequestMapper {

	private final NovelMapper novelMapper;
	private final UserMapper userMapper;
	private final ChapterMapper chapterMapper;

	public RequestDTO toResponse(BaseRequest request) {
		return switch (request) {
			case ChapterRequest chapterRequest -> mapToChapterRequest(chapterRequest);
			case NovelRequest novelRequest -> mapToNovelRequest(novelRequest);
			case FixChapterRequest fixChapterRequest -> mapToFixChapterRequestDTO(fixChapterRequest);
			case null, default -> throw new InternalServerError(
					"Unsupported Request type: " + Objects.requireNonNull(request).getClass().getName()
			);

		};
	}

	private ChapterRequestDTO mapToChapterRequest(ChapterRequest chapterRequest) {
		var novelInfoDTO = novelMapper.toInfoResponse(chapterRequest.getNovel());
		var userDTO = userMapper.toResponse(chapterRequest.getUser());
		return new ChapterRequestDTO(
				chapterRequest.getId(),
				chapterRequest.getStatus().name(),
				chapterRequest.getRequestType(),
				chapterRequest.getCreatedAt(),
				novelInfoDTO,
				userDTO
		);
	}

	private NovelRequestDTO mapToNovelRequest(NovelRequest request) {
		var userDTO = userMapper.toResponse(request.getUser());
		return new NovelRequestDTO(
				request.getId(),
				request.getStatus().name(),
				request.getRequestType(),
				request.getCreatedAt(),
				request.getNovelTitle(),
				userDTO
		);
	}

	private FixChapterRequestDTO mapToFixChapterRequestDTO(FixChapterRequest request) {
		var userDTO = userMapper.toResponse(request.getUser());
		var chapterInfo = chapterMapper.toInfo(request.getChapter());
		return new FixChapterRequestDTO(
				request.getId(),
				request.getStatus().name(),
				request.getRequestType(),
				request.getCreatedAt(),
				userDTO,
				chapterInfo,
				request.getErrors(),
				request.getAnotherReason()
		);
	}
}
