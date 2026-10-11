package miralhas.github.stalkers.domain.model.series;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import miralhas.github.stalkers.domain.model.novel.Novel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static miralhas.github.stalkers.StalkersApplication.SLG;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Series implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false)
	private String title;

	@JsonIgnore
	@OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = {
			CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST
	})
	private Set<Novel> books = new HashSet<>();

	@CreationTimestamp
	@Column(nullable = true)
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = true)
	private OffsetDateTime updatedAt;

	public void addBook(Novel novel) {
		this.books.add(novel);
		novel.setSeries(this);
	}

	public void addAllBooks(List<Novel> books) {
		this.books.addAll(books);
		books.forEach(b -> b.setSeries(this));
	}

	public void removeAllBooks() {
		this.books.forEach(b -> b.setSeries(null));
		this.books.clear();
	}

	public void removeBook(Novel novel) {
		this.books.remove(novel);
		novel.setSeries(null);
	}

	public void generateSlug() {
		this.slug = SLG.slugify(title);
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Series series = (Series) o;
		return getId() != null && Objects.equals(getId(), series.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
