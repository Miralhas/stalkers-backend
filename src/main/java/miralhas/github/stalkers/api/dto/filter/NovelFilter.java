package miralhas.github.stalkers.api.dto.filter;

import miralhas.github.stalkers.config.validation.EnumPattern;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.model.novel.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static miralhas.github.stalkers.infrastructure.repository.NovelSpec.*;

public record NovelFilter(
		String status,
		String q,
		List<String> tags,
		List<String> genres,
		String author,
		Boolean showAll,
		ChapterCount chapterCount,
		ChaptersRange chaptersRange
) {

	public NovelFilter(@EnumPattern(enumClass = Status.class)
					   String status, String q, List<String> tags, List<String> genres, String author, Boolean showAll,
					   ChapterCount chapterCount, ChaptersRange chaptersRange
	) {
		this.status = status;
		this.q = q;
		this.tags = tags;
		this.genres = genres;
		this.author = author;
		this.showAll = showAll == null ? Boolean.FALSE : showAll;
		this.chapterCount = chapterCount;
		this.chaptersRange = chaptersRange;
	}

	public Specification<Novel> toSpecification() {
		return titleContains(q)
				.and(authorLike(author))
				.and(statusEquals(status))
				.and(tagsNameEqual(tags))
				.and(genresNameEqual(genres))
				.and(showEveryNovel(showAll))
				.and(chaptersCount(chapterCount))
				.and(chaptersRangeBetween(chaptersRange));
	}

}


