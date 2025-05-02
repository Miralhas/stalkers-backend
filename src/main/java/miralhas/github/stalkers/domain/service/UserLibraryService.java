package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.UserLibraryDTO;
import miralhas.github.stalkers.api.dto_mapper.UserLibraryMapper;
import miralhas.github.stalkers.domain.exception.BusinessException;
import miralhas.github.stalkers.domain.model.UserLibrary;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.UserLibraryRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLibraryService {

	private final UserLibraryRepository userLibraryRepository;
	private final UserLibraryMapper userLibraryMapper;
	private final NovelService novelService;
	private final ChapterService chapterService;
	private final ErrorMessages errorMessages;

	public List<UserLibraryDTO> findUserLibrary(User user) {
		List<Object[]> userLibrary = userLibraryRepository.findUserLibraryByUserId(user.getId());
		return userLibrary.stream().map(libraryNovel -> {
			var userHistoryDTO = userLibraryMapper.toResponse((UserLibrary) libraryNovel[0]);
			userHistoryDTO.setTotalChapters((Long) libraryNovel[1]);
			return userHistoryDTO;
		}).toList();
	}

	@Transactional
	public UserLibraryDTO updateUserLibrary(User user, Long novelId, Long chapterId) {
		var novel = novelService.findByIdOrException(novelId);
		var chapter = chapterService.findByIdOrException(chapterId);
		validateLibrary(novel, chapter);

		var optionalNovelHistory = userLibraryRepository
				.findNovelInUserLibraryByUserIdAndNovelId(novel.getId(), user.getId());

		UserLibrary userLibrary;

		// user já tem essa novel na biblioteca.
		if (optionalNovelHistory.isPresent()) {
			var novelHistory = optionalNovelHistory.get();
			novelHistory.setCurrentChapter(chapter);
			userLibrary = userLibraryRepository.save(novelHistory);
			var userHistoryDTO = userLibraryMapper.toResponse(userLibrary);
			userHistoryDTO.setTotalChapters(null);
			return userHistoryDTO;
		}

		userLibrary = UserLibrary.builder()
				.user(user)
				.currentChapter(chapter)
				.novel(novel)
				.build();

		var userHistoryDTO = userLibraryMapper.toResponse(userLibraryRepository.save(userLibrary));
		userHistoryDTO.setTotalChapters(null);
		return userHistoryDTO;
	}


	private void validateLibrary(Novel novel, Chapter chapter) {
		if (novel.equals(chapter.getNovel())) return;
		throw new BusinessException(
				errorMessages.get("library.invalidChapter", chapter.getSlug(), novel.getSlug())
		);
	}

}
