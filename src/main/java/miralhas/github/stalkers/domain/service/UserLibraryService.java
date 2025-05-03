package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.UserLibraryDTO;
import miralhas.github.stalkers.api.dto.filter.LibraryFilter;
import miralhas.github.stalkers.api.dto_mapper.UserLibraryMapper;
import miralhas.github.stalkers.domain.exception.BusinessException;
import miralhas.github.stalkers.domain.exception.LibraryElementNotFound;
import miralhas.github.stalkers.domain.model.UserLibrary;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.UserLibraryRepository;
import miralhas.github.stalkers.domain.utils.CacheManagerUtils;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.cache.annotation.Cacheable;
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
	private final CacheManagerUtils cacheManagerUtils;

	public UserLibrary getLibraryElementOrException(Long libraryElementId) {
		return userLibraryRepository.findById(libraryElementId).orElseThrow(() -> new LibraryElementNotFound(
				errorMessages.get("library.notFound.id", libraryElementId)
		));
	}

	@Cacheable(
			cacheNames = "libraries.list",
			key = "{#user.getEmail(), #filter, #pageable}",
			unless = "#result.getContent().isEmpty()"
	)
	public Page<UserLibraryDTO> findUserLibrary(User user, LibraryFilter filter, Pageable pageable) {
		Page<Object[]> userLibrary;
		if (filter.getBookmarked()) {
			userLibrary = userLibraryRepository.findUserLibraryBookmarkByUserId(user.getId(), pageable);
		} else if (filter.getCompleted()) {
			userLibrary = userLibraryRepository.findUserLibraryCompletedByUserId(user.getId(), pageable);
		} else {
			userLibrary = userLibraryRepository.findUserLibraryByUserId(user.getId(), pageable);
		}
		var userLibraryPageDTO = userLibrary.getContent().stream().map(libraryNovel -> {
			var userHistoryDTO = userLibraryMapper.toResponse((UserLibrary) libraryNovel[0]);
			userHistoryDTO.setTotalChapters((Long) libraryNovel[1]);
			return userHistoryDTO;
		}).toList();
		return new PageImpl<>(userLibraryPageDTO, pageable, userLibrary.getTotalElements());
	}

	@Transactional
	public void updateUserLibrary(User user, Long novelId, Long chapterId) {
		var novel = novelService.findByIdOrException(novelId);
		var chapter = chapterService.findByIdOrException(chapterId);
		validateIfChapterIsFromNovel(novel, chapter);

		var optionalNovelHistory = userLibraryRepository
				.findNovelInUserLibraryByUserIdAndNovelId(novel.getId(), user.getId());

		UserLibrary userLibrary;

		cacheManagerUtils.evictUserLibraryEntry(user.getEmail());

		// user já tem essa novel na biblioteca.
		if (optionalNovelHistory.isPresent()) {
			var novelHistory = optionalNovelHistory.get();
			novelHistory.setCurrentChapter(chapter);
			novelHistory.setLastReadAt(OffsetDateTime.now());
			userLibraryRepository.save(novelHistory);
			return;
		}

		userLibrary = UserLibrary.builder()
				.user(user)
				.currentChapter(chapter)
				.novel(novel)
				.lastReadAt(OffsetDateTime.now())
				.build();

		userLibraryRepository.save(userLibrary);
	}

	@Transactional
	public void bookmarkNovel(User user, Novel novel) {
		var libraryNovelOptional = userLibraryRepository
				.findNovelInUserLibraryByUserIdAndNovelId(novel.getId(), user.getId());

		cacheManagerUtils.evictUserLibraryEntry(user.getEmail());

		if (libraryNovelOptional.isPresent()) {
			var libraryNovel = libraryNovelOptional.get();
			libraryNovel.setBookmarked(true);
			userLibraryRepository.save(libraryNovel);
			return;
		}

		var novelLibrary = UserLibrary.builder()
				.user(user)
				.novel(novel)
				.isBookmarked(true)
				.lastReadAt(OffsetDateTime.now())
				.build();

		userLibraryRepository.save(novelLibrary);
	}

	@Transactional
	public void libraryNovelCompleted(User user, Novel novel) {
		var libraryNovelOptional = userLibraryRepository
				.findNovelInUserLibraryByUserIdAndNovelId(novel.getId(), user.getId());

		cacheManagerUtils.evictUserLibraryEntry(user.getEmail());

		if (libraryNovelOptional.isPresent()) {
			var libraryNovel = libraryNovelOptional.get();
			libraryNovel.setCompleted(true);
			userLibraryRepository.save(libraryNovel);
			return;
		}

		var novelLibrary = UserLibrary.builder()
				.user(user)
				.currentChapter(null)
				.novel(novel)
				.isBookmarked(true)
				.isCompleted(true)
				.lastReadAt(OffsetDateTime.now())
				.build();

		userLibraryRepository.save(novelLibrary);
	}

	@Transactional
	public void removeBookmark(Long libraryElementId) {
		var lib = getLibraryElementOrException(libraryElementId);
		lib.setBookmarked(false);
		lib = userLibraryRepository.save(lib);
		cacheManagerUtils.evictUserLibraryEntry(lib.getUser().getEmail());
	}

	@Transactional
	public void removeComplete(Long libraryElementId) {
		var lib = getLibraryElementOrException(libraryElementId);
		lib.setCompleted(false);
		userLibraryRepository.save(lib);
		lib = userLibraryRepository.save(lib);
		cacheManagerUtils.evictUserLibraryEntry(lib.getUser().getEmail());
	}

	private void validateIfChapterIsFromNovel(Novel novel, Chapter chapter) {
		if (novel.equals(chapter.getNovel())) return;
		throw new BusinessException(
				errorMessages.get("library.invalidChapter", chapter.getSlug(), novel.getSlug())
		);
	}

}
