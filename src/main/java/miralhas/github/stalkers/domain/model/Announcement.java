package miralhas.github.stalkers.domain.model;

import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.utils.CommonsUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static miralhas.github.stalkers.StalkersApplication.SLG;

@Entity
@Getter
@Setter
public class Announcement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String body;

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	private User user;

	@CreationTimestamp
	@Column(nullable = true)
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = true)
	private OffsetDateTime updatedAt;

	@Column(nullable = false)
	private boolean pinned = false;

	public void generateSlug() {
		var titleArr = this.title.split(" ");
		var truncatedTitle = Arrays.stream(titleArr).limit(9).collect(Collectors.joining(" "));
		this.slug = SLG.slugify(truncatedTitle);
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Announcement that = (Announcement) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
