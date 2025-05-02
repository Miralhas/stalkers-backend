package miralhas.github.stalkers.domain.model;

import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.novel.Chapter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
		name = "user_library",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "novel_id"})}
)
public class UserLibrary implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "novel_id", nullable = false)
	private Novel novel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_chapter_id", nullable = false)
	private Chapter currentChapter;

//	@UpdateTimestamp
	@Column(name = "last_read_at", nullable = false)
	private OffsetDateTime lastReadAt;

	@Column(nullable = false)
	private boolean isBookmarked;

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		UserLibrary userLibrary = (UserLibrary) o;
		return getId() != null && Objects.equals(getId(), userLibrary.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}