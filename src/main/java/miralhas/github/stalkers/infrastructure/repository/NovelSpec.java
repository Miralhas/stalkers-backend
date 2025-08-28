package miralhas.github.stalkers.infrastructure.repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import miralhas.github.stalkers.api.dto.filter.ChapterCount;
import miralhas.github.stalkers.domain.model.novel.Genre;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.model.novel.Tag;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;

@UtilityClass
public class NovelSpec {

	public static Specification<Novel> titleContains(String q) {
		return (root, query, builder) -> {
			if (!hasText(q)) return null;
			return builder.or(
					builder.like(root.get("title"), "%" + q.toLowerCase() + "%"),
					builder.like(root.get("alias"), "%" + q.toLowerCase() + "%")
			);
		};
	}

	public static Specification<Novel> chaptersCountRange(ChapterCount count) {
		return (root, query, builder) -> {
			if (Objects.isNull(count)) return null;
			return switch (count.getOperator()) {
				case GTE -> builder.greaterThanOrEqualTo(root.get("chaptersCount"), count.getCount());
				case LTE -> builder.lessThanOrEqualTo(root.get("chaptersCount"), count.getCount());
			};
		};
	}

	public static Specification<Novel> authorLike(String author) {
		return (root, query, builder) -> {
			if (!hasText(author)) return null;
			return builder.like(
					root.get("author"), "%" + author.toLowerCase() + "%"
			);
		};
	}

	public static Specification<Novel> statusEquals(String status) {
		return (root, query, builder) -> {
			if (!hasText(status)) return null;
			return builder.equal(root.get("status"), status);
		};
	}

	// By default, the query will show only novels that have the 'isHidden' flag set to false.
	// When requesting the query, if the query param 'showAll' is set to true, then it will be shown all novels, be it
	// the flag false or not.
	public static Specification<Novel> showEveryNovel(Boolean flag) {
		return (root, query, builder) -> {
			if (flag == null || flag) return null;
			return builder.equal(root.get("isHidden"), false);
		};
	}

	public static Specification<Novel> tagsNameEqual(List<String> tags) {
		return (root, query, builder) -> {
			if (!ObjectUtils.isEmpty(tags) && Objects.nonNull(query)) {
				Join<Novel, Tag> tagsJoin = root.join("tags");
				Predicate tagPredicate = tagsJoin.get("name").in(tags);
				query.groupBy(root.get("id"));
				query.having(builder.equal(builder.countDistinct(tagsJoin.get("id")), tags.size()));
				return builder.and(tagPredicate);
			}
			return null;
		};
	}

	public static Specification<Novel> genresNameEqual(List<String> genres) {
		return (root, query, builder) -> {
			if (!ObjectUtils.isEmpty(genres) && Objects.nonNull(query)) {
				Join<Novel, Genre> genresJoin = root.join("genres");
				Predicate genrePredicate = genresJoin.get("name").in(genres);
				query.groupBy(root.get("id"));
				query.having(builder.equal(builder.countDistinct(genresJoin.get("id")), genres.size()));
				return builder.and(genrePredicate);
			}
			return null;
		};
	}

}
