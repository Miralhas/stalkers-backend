package miralhas.github.stalkers.domain.model.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import miralhas.github.stalkers.domain.model.novel.Chapter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@DiscriminatorValue(Comment.CHAPTER_REVIEW)
public class ChapterReview extends Comment implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public ChapterReview() {
		super();
	}

	@JoinColumn(name = "chapter_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Chapter chapter;

	public String getNovelTitle() {
		return this.chapter.getNovel().capitalizedTitle();
	}

	public String getChapterTitle() {
		return this.chapter.capitalizedTitle();
	}

}
