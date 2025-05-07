package miralhas.github.stalkers.api.dto_mapper;

import miralhas.github.stalkers.api.dto.ChapterSummaryDTO;
import miralhas.github.stalkers.api.dto.NovelDTO;
import miralhas.github.stalkers.api.dto.NovelSummaryDTO;
import miralhas.github.stalkers.api.dto.input.NovelInput;
import miralhas.github.stalkers.api.dto.input.UpdateNovelInput;
import miralhas.github.stalkers.domain.model.novel.Genre;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.model.novel.Tag;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class NovelMapper {

	@Autowired
	protected NovelRepository novelRepository;

	@Mapping(target = "tags", qualifiedByName = "tagsInputMapper")
	@Mapping(target = "genres", qualifiedByName = "genresInputMapper")
	public abstract Novel fromInput(NovelInput novelInput);

	@Mapping(target = "tags", qualifiedByName = "tagsMapper")
	@Mapping(target = "genres", qualifiedByName = "genresMapper")
	@Mapping(target = "firstChapter", expression = "java(getFirstChapter(novel))")
	@Mapping(target = "lastChapter", expression = "java(getLastChapter(novel))")
	public abstract NovelDTO toResponse(Novel novel);

	public abstract NovelSummaryDTO toSummaryResponse(Novel novel);

	public abstract List<NovelDTO> toCollectionResponse(List<Novel> novels);

	public abstract List<NovelSummaryDTO> toSummaryCollectionResponse(List<Novel> novels);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "tags", qualifiedByName = "tagsInputMapper")
	@Mapping(target = "genres", qualifiedByName = "genresInputMapper")
	public abstract Novel update(UpdateNovelInput input, @MappingTarget Novel novel);

	@Named("getFirstChapter")
	ChapterSummaryDTO getFirstChapter(Novel novel) {
		return novelRepository.findNovelFirstChapterByNovelId(novel.getId());
	}

	@Named("getLastChapter")
	ChapterSummaryDTO getLastChapter(Novel novel) {
		return novelRepository.findNovelLastChapterByNovelId(novel.getId());
	}

	@Named("tagsMapper")
	String tagsMapper(Tag tag) {
		return tag.getName();
	}

	@Named("genresMapper")
	String genresMapper(Genre genre) {
		return genre.getName();
	}

	@Named("tagsInputMapper")
	Set<Tag> tagsInputToEntityMapper(List<String> tags) {
		if (ObjectUtils.isEmpty(tags)) return null;
		return tags.stream().map(Tag::new).collect(Collectors.toSet());
	}

	@Named("genresInputMapper")
	Set<Genre> genresInputToEntityMapper(List<String> genres) {
		if (ObjectUtils.isEmpty(genres)) return null;
		return genres.stream().map(Genre::new).collect(Collectors.toSet());
	}

}