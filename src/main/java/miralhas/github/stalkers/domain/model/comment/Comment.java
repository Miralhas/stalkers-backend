package miralhas.github.stalkers.domain.model.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miralhas.github.stalkers.domain.model.auth.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "comments")
@DiscriminatorValue("COMMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name = "comment_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Comment implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	public static final String NOVEL_REVIEW = "NOVEL_REVIEW";
	public static final String CHAPTER_REVIEW = "CHAPTER_REVIEW";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String message;

	@ManyToOne(fetch = FetchType.LAZY)
	private Comment parentComment;

	@OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Comment> childComments = new ArrayList<>();

	@CreationTimestamp
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	private OffsetDateTime updatedAt;

	@Column(nullable = false)
	private Boolean isSpoiler = Boolean.FALSE;

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User commenter;

	@OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Vote> votes;

	@Formula("(SELECT COALESCE(SUM(v.count), 0) FROM vote v WHERE v.comment_id = id)")
	private long voteCount;

	public boolean hasParent() {
		return this.parentComment != null;
	}

	public Long getParentId() {
		if (!hasParent()) return null;
		return this.getParentComment().getId();
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Comment comment = (Comment) o;
		return getId() != null && Objects.equals(getId(), comment.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
