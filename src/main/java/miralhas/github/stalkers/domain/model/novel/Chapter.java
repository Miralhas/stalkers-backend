package miralhas.github.stalkers.domain.model.novel;


import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.utils.CommonsUtils;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static miralhas.github.stalkers.StalkersApplication.SLG;

@With
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chapter implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private Long number;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String body;

	@Column(nullable = false, unique = true)
	private String slug;

	@ManyToOne(fetch = FetchType.LAZY)
	private Novel novel;

	public void generateSlug(String novelTitle) {
		var titleInitials = CommonsUtils.getInitialsFromSlug(SLG.slugify(novelTitle));
		this.slug = SLG.slugify("%s chapter %d".formatted(titleInitials, this.number));
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Chapter chapter = (Chapter) o;
		return getId() != null && Objects.equals(getId(), chapter.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
