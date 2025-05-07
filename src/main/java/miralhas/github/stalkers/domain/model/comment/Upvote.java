package miralhas.github.stalkers.domain.model.comment;

import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.model.auth.User;

import java.io.Serial;
import java.io.Serializable;

@With
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Upvote implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Comment comment;
}
