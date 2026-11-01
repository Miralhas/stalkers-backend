package miralhas.github.stalkers.domain.comment.model;

import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.user.model.User;
import miralhas.github.stalkers.domain.comment.model.enums.Type;

import java.io.Serial;
import java.io.Serializable;

@With
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private Comment comment;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(nullable = false)
	private int count;
}
