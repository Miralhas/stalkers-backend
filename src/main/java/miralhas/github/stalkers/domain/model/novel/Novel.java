package miralhas.github.stalkers.domain.model.novel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.model.Image;
import miralhas.github.stalkers.domain.model.comment.NovelReview;
import miralhas.github.stalkers.domain.model.novel.enums.Status;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static miralhas.github.stalkers.StalkersApplication.SLG;

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
	private String title;

	@Column(nullable = false)
	private String author;

	@Builder.Default
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.ON_GOING;

	@Column(nullable = true)
	private String alias;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@CreationTimestamp
	@Column(nullable = true)
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = true)
	private OffsetDateTime updatedAt;

	@Builder.Default
	@Column(nullable = false)
	private Boolean isHidden = Boolean.FALSE;

	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
	)
	@JoinTable(
			name = "novel_tags",
			joinColumns = @JoinColumn(name = "novel_id"), inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tag> tags = new HashSet<>();

	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
	)
	@JoinTable(
			name = "novel_genres",
			joinColumns = @JoinColumn(name = "novel_id"), inverseJoinColumns = @JoinColumn(name = "genre_id")
	)
	private Set<Genre> genres = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "novel", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<NovelReview> reviews;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Image image;

	@Formula("(SELECT COUNT(*) FROM chapter c WHERE c.novel_id = id ORDER BY c.id)")
	private long chaptersCount;

	public void addReview(NovelReview review) {
		this.reviews.add(review);
		review.setNovel(this);
	}

	public void removeReview(NovelReview review) {
		this.reviews.remove(review);
		review.setNovel(null);
	}

	public String capitalizedTitle() {
		return Arrays.stream(this.title.split("\\s+"))
				.map(StringUtils::capitalize)
				.collect(Collectors.joining(" "));
	}

	public void generateSlug() {
		this.slug = SLG.slugify(title);
	}

	public boolean hasImage() {
		return this.image != null;
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