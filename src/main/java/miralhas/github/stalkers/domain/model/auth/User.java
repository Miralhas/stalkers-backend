package miralhas.github.stalkers.domain.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.model.Image;
import miralhas.github.stalkers.domain.model.UserLibrary;
import miralhas.github.stalkers.domain.model.comment.ChapterReview;
import miralhas.github.stalkers.domain.model.comment.NovelReview;
import miralhas.github.stalkers.domain.model.comment.Vote;
import miralhas.github.stalkers.domain.model.metrics.Rating;
import miralhas.github.stalkers.domain.model.notification.NotificationRecipient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
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
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	private String password;

	@Builder.Default
	@Column(nullable = false, name = "is_oauth2_authenticated")
	private Boolean isOAuth2Authenticated = false;

	@CreationTimestamp
	private OffsetDateTime createdAt;

	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
	)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Image image;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserLibrary> userLibraries;

	@OneToMany(mappedBy = "commenter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChapterReview> chapterComments;

	@OneToMany(mappedBy = "commenter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NovelReview> novelReviews;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Vote> votes;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rating> ratings;

	@OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NotificationRecipient> recipients;

	public List<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(r -> r.getName().getAuthority()).toList();
	}

	@JsonIgnore
	public boolean isAdmin() {
		return roles.stream().anyMatch(r -> r.getName().equals(Role.Value.ADMIN));
	}

	@JsonIgnore
	public boolean hasImage() {
		return this.image != null;
	}

	public String getImageFileName() {
		if (hasImage()) return this.image.getFileName();
		return null;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		User user = (User) o;
		return getId() != null && Objects.equals(getId(), user.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}

}