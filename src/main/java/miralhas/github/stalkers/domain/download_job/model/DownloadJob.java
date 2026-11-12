package miralhas.github.stalkers.domain.download_job.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import miralhas.github.stalkers.domain.download_job.model.enums.JobStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DownloadJob implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String slug;

	@Column(nullable = false)
	private Integer start;

	@Column(nullable = false)
	private Integer end;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private JobStatus status = JobStatus.PENDING;

	@Column(nullable = true)
	private String path;

	@Column(nullable = true)
	private String error;

	@Column(nullable = true)
	private String exclude;

	@CreationTimestamp
	@Column(nullable = true)
	private OffsetDateTime createdAt;

	public Path getFilePath() {
		if (!StringUtils.hasText(path)) return null;
		return Path.of(this.path);
	}

	public String getNormalizedFilename() {
		var path = getFilePath();
		if (Objects.isNull(path)) return null;
		return path.getFileName().toString().replace("%d.".formatted(id), "");
	}

	public boolean failed() {
		return this.status.equals(JobStatus.FAILED);
	}

	public boolean completed() {
		return this.status.equals(JobStatus.COMPLETED);
	}

	public boolean pending() {
		return this.status.equals(JobStatus.PENDING);
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		DownloadJob that = (DownloadJob) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
