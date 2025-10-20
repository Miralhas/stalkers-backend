package miralhas.github.stalkers.domain.model.requests;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@With
@AllArgsConstructor
@DiscriminatorValue(BaseRequest.NOVEL_REQUEST)
public class NovelRequest extends BaseRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public NovelRequest() {
		super();
	}

	@Column(nullable = true)
	private String novelTitle;

}
