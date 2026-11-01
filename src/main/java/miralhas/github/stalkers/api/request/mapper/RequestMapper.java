package miralhas.github.stalkers.api.request.mapper;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.chapter.dto.ChapterRequest;
import miralhas.github.stalkers.api.chapter.mapper.ChapterMapper;
import miralhas.github.stalkers.api.user.mapper.UserMapper;
import miralhas.github.stalkers.api.novel.mapper.NovelMapper;
import miralhas.github.stalkers.api.request.dto.FixChapterRequest;
import miralhas.github.stalkers.api.request.dto.NovelRequest;
import miralhas.github.stalkers.domain.request.interfaces.Request;
import miralhas.github.stalkers.domain.shared.exception.InternalServerError;
import miralhas.github.stalkers.domain.request.model.BaseRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RequestMapper {

	private final NovelMapper novelMapper;
	private final UserMapper userMapper;
	private final ChapterMapper chapterMapper;

	public Request toResponse(BaseRequest request) {
		return switch (request) {
			case miralhas.github.stalkers.domain.request.model.ChapterRequest chapterRequest -> mapToChapterRequest(chapterRequest);
			case miralhas.github.stalkers.domain.request.model.NovelRequest novelRequest -> mapToNovelRequest(novelRequest);
			case miralhas.github.stalkers.domain.request.model.FixChapterRequest fixChapterRequest -> mapToFixChapterRequestDTO(fixChapterRequest);
			case null, default -> throw new InternalServerError(
					"Unsupported Request type: " + Objects.requireNonNull(request).getClass().getName()
			);

		};
	}

	private ChapterRequest mapToChapterRequest(miralhas.github.stalkers.domain.request.model.ChapterRequest chapterRequest) {
		var novelInfoDTO = novelMapper.toInfoResponse(chapterRequest.getNovel());
		var userDTO = userMapper.toResponse(chapterRequest.getUser());
		return new ChapterRequest(
				chapterRequest.getId(),
				chapterRequest.getStatus().name(),
				chapterRequest.getRequestType(),
				chapterRequest.getCreatedAt(),
				novelInfoDTO,
				userDTO
		);
	}

	private NovelRequest mapToNovelRequest(miralhas.github.stalkers.domain.request.model.NovelRequest request) {
		var userDTO = userMapper.toResponse(request.getUser());
		return new NovelRequest(
				request.getId(),
				request.getStatus().name(),
				request.getRequestType(),
				request.getCreatedAt(),
				request.getNovelTitle(),
				userDTO
		);
	}

	private FixChapterRequest mapToFixChapterRequestDTO(miralhas.github.stalkers.domain.request.model.FixChapterRequest request) {
		var userDTO = userMapper.toResponse(request.getUser());
		var chapterInfo = chapterMapper.toInfo(request.getChapter());
		return new FixChapterRequest(
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
