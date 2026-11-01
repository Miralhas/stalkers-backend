package miralhas.github.stalkers.domain.comment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import miralhas.github.stalkers.domain.novel.model.Novel;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@With
@AllArgsConstructor
@DiscriminatorValue(Comment.NOVEL_REVIEW)
public class NovelReview extends Comment implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public NovelReview() {
		super();
	}

	@JoinColumn(name = "novel_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Novel novel;

	public String getNovelTitle() {
		return this.novel.capitalizedTitle();
	}
}
