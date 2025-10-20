package miralhas.github.stalkers.domain.model.requests;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import miralhas.github.stalkers.domain.model.novel.Novel;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@With
@AllArgsConstructor
@DiscriminatorValue(BaseRequest.CHAPTER_REQUEST)
public class ChapterRequest extends BaseRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public ChapterRequest() {
		super();
	}

	@JoinColumn(name = "novel_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Novel novel;
}
