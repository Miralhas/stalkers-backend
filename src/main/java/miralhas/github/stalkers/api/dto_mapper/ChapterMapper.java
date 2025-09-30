package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.ChapterDTO;
import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.input.ChapterInput;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.ChapterRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS
)
public abstract class ChapterMapper {

	@Autowired
	protected ChapterRepository chapterRepository;

	public abstract Chapter fromInput(ChapterInput chapterInput);
	public abstract List<Chapter> fromInputCollection(List<ChapterInput> chapterInputList);

	@Mapping(target = "novelSlug", source = "novel.slug")
	@Mapping(target = "novelId", source = "novel.id")
	@Mapping(target = "novelTitle", source = "novel.title")
	@Mapping(target = "novelChaptersCount", source = "novel.chaptersCount")
	@Mapping(target = "previous", expression = "java(getPreviousChapter(chapter))")
	@Mapping(target = "next", expression = "java(getNextChapter(chapter))")
	public abstract ChapterDTO toResponse(Chapter chapter);

	public abstract ChapterSummaryDTO toSummaryResponse(Chapter chapter);

	public abstract List<ChapterSummaryDTO> toSummaryCollectionResponse(List<Chapter> chapters);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "slug", ignore = true)
	public abstract Chapter update(ChapterInput chapterInput, @MappingTarget Chapter chapter);

	@Named("getPreviousChapter")
	ChapterSummaryDTO getPreviousChapter(Chapter chapter) {
		if (chapter.getNumber() <= 1) return null;
		var chapterSummary = chapterRepository.findPreviousChapter(chapter.getNovel().getId(), chapter.getNumber());
		if (Objects.isNull(chapterSummary) || Objects.isNull(chapterSummary.id())) return null;
		return chapterSummary;
	}

	@Named("getNextChapter")
	ChapterSummaryDTO getNextChapter(Chapter chapter) {
		var chapterSummary = chapterRepository.findNextChapter(chapter.getNovel().getId(), chapter.getNumber());
		if (Objects.isNull(chapterSummary) || Objects.isNull(chapterSummary.id())) return null;
		return chapterSummary;
	}

}