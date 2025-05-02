package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.UserLibraryDTO;
import miralhas.github.stalkers.api.dto_mapper.UserLibraryMapper;
import miralhas.github.stalkers.domain.exception.BusinessException;
import miralhas.github.stalkers.domain.model.UserLibrary;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.ChapterRepository;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.repository.UserLibraryRepository;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLibraryService {

	private final UserLibraryRepository userLibraryRepository;
	private final UserLibraryMapper userLibraryMapper;
	private final NovelService novelService;
	private final ChapterService chapterService;
	private final ErrorMessages errorMessages;
	private final NovelRepository novelRepository;
	private final ChapterRepository chapterRepository;

	public Page<UserLibraryDTO> findUserLibrary(User user, Boolean bookmarked, Pageable pageable) {
		var userLibrary = bookmarked
				? userLibraryRepository.findUserLibraryBookmarkByUserId(user.getId(), pageable)
				: userLibraryRepository.findUserLibraryByUserId(user.getId(), pageable);

		var userLibraryPageDTO = userLibrary.getContent().stream().map(libraryNovel -> {
			var userHistoryDTO = userLibraryMapper.toResponse((UserLibrary) libraryNovel[0]);
			userHistoryDTO.setTotalChapters((Long) libraryNovel[1]);
			return userHistoryDTO;
		}).toList();

		return new PageImpl<>(userLibraryPageDTO, pageable, userLibrary.getTotalElements());
	}

	@Transactional
	public UserLibraryDTO updateUserLibrary(User user, Long novelId, Long chapterId) {
		var novel = novelService.findByIdOrException(novelId);
		var chapter = chapterService.findByIdOrException(chapterId);
		validateIfChapterIsFromNovel(novel, chapter);

		var optionalNovelHistory = userLibraryRepository
				.findNovelInUserLibraryByUserIdAndNovelId(novel.getId(), user.getId());

		UserLibrary userLibrary;

		// user já tem essa novel na biblioteca.
		if (optionalNovelHistory.isPresent()) {
			var novelHistory = optionalNovelHistory.get();
			novelHistory.setCurrentChapter(chapter);
			novelHistory.setLastReadAt(OffsetDateTime.now());
			userLibrary = userLibraryRepository.save(novelHistory);
			var userHistoryDTO = userLibraryMapper.toResponse(userLibrary);
			userHistoryDTO.setTotalChapters(null);
			return userHistoryDTO;
		}

		userLibrary = UserLibrary.builder()
				.user(user)
				.currentChapter(chapter)
				.novel(novel)
				.lastReadAt(OffsetDateTime.now())
				.build();

		var userHistoryDTO = userLibraryMapper.toResponse(userLibraryRepository.save(userLibrary));
		userHistoryDTO.setTotalChapters(null);
		return userHistoryDTO;
	}


	@Transactional
	public void bookmarkNovel(User user, Novel novel) {
		var libraryNovelOptional = userLibraryRepository
				.findNovelInUserLibraryByUserIdAndNovelId(novel.getId(), user.getId());

		if (libraryNovelOptional.isPresent()) {
			var libraryNovel = libraryNovelOptional.get();
			libraryNovel.setBookmarked(true);
			userLibraryRepository.save(libraryNovel);
			return;
		}

		var novelFirstChapterId = novelRepository.getNovelFirstChapterIdByNovelId(novel.getId());
		var novelLibrary = UserLibrary.builder()
				.user(user)
				.currentChapter(chapterRepository.getReferenceById(novelFirstChapterId))
				.novel(novel)
				.isBookmarked(true)
				.lastReadAt(OffsetDateTime.now())
				.build();

		userLibraryRepository.save(novelLibrary);
	}


	private void validateIfChapterIsFromNovel(Novel novel, Chapter chapter) {
		if (novel.equals(chapter.getNovel())) return;
		throw new BusinessException(
				errorMessages.get("library.invalidChapter", chapter.getSlug(), novel.getSlug())
		);
	}

}
