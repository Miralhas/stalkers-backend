package miralhas.github.stalkers.domain.model.novel;


import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.model.comment.ChapterReview;
import miralhas.github.stalkers.domain.utils.CommonsUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
	@SequenceGenerator(name = "seqGen", sequenceName = "chap_seq", initialValue = 1)
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

	@CreationTimestamp
	@Column(nullable = true)
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = true)
	private OffsetDateTime updatedAt;

	@OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<ChapterReview> reviews;

	public void addReview(ChapterReview review) {
		this.reviews.add(review);
		review.setChapter(this);
	}

	public void removeReview(ChapterReview review) {
		this.reviews.remove(review);
		review.setChapter(null);
	}

	public void generateSlug(String novelTitle) {
		var titleInitials = CommonsUtils.getInitialsFromSlug(SLG.slugify(novelTitle));
		this.slug = SLG.slugify("%s chapter %d".formatted(titleInitials, this.number));
	}

	public String capitalizedTitle() {
		return CommonsUtils.capitalize(this.title);
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
