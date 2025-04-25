package miralhas.github.stalkers.domain.model.novel;

import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.model.Image;
import miralhas.github.stalkers.domain.model.novel.enums.Status;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@With
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Novel implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Chapter> chapters;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String author;

	@Builder.Default
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.ON_GOING;

	@Column(nullable = true)
	private String description;

	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
	)
	@JoinTable(
			name = "novel_tags",
			joinColumns = @JoinColumn(name = "novel_id"), inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tags> tags = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Image image;

	public long novelChaptersCount() {
		return this.chapters.size();
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Novel novel = (Novel) o;
		return getId() != null && Objects.equals(getId(), novel.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}