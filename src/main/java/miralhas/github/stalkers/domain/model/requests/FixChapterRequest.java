package miralhas.github.stalkers.domain.model.requests;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import miralhas.github.stalkers.domain.model.novel.Chapter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@With
@AllArgsConstructor
@DiscriminatorValue(BaseRequest.FIX_CHAPTER_REQUEST)
public class FixChapterRequest extends BaseRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public FixChapterRequest() {
		super();
	}

	@JoinColumn(name = "chapter_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Chapter chapter;

	@ManyToMany(
			fetch = FetchType.EAGER,
			cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
	)
	@JoinTable(
			name = "fix_chapter_request_errors",
			joinColumns = @JoinColumn(name = "request_id"),
			inverseJoinColumns = @JoinColumn(name = "error_id")
	)
	private Set<ChapterError> errors;

	@Column(nullable = true, columnDefinition = "TEXT")
	private String anotherReason;
}
