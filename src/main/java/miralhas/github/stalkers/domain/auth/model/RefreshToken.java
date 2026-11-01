package miralhas.github.stalkers.domain.auth.model;

import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.user.model.User;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	private UUID id;

	@CreationTimestamp
	private OffsetDateTime createdAt;

	@Column(nullable = false)
	private OffsetDateTime expiresAt;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public boolean isInvalid() {
		return expiresAt.isBefore(OffsetDateTime.now());
	}
}
