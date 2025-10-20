package miralhas.github.stalkers.domain.model.requests;

import jakarta.persistence.*;
import lombok.*;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.model.requests.enums.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "request")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "request_type")
public class BaseRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	public static final String NOVEL_REQUEST = "NOVEL_REQUEST";
	public static final String CHAPTER_REQUEST = "CHAPTER_REQUEST";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(nullable = true)
	private OffsetDateTime createdAt;

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING;

	@Column(name="request_type", insertable = false, updatable = false)
	protected String requestType;

	public void complete() {
		this.status = Status.COMPLETED;
	}

	public void deny() {
		this.status = Status.DENIED;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		BaseRequest that = (BaseRequest) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
