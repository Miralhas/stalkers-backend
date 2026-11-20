package miralhas.github.stalkers.api.user.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public interface UserInfoProjection {
	Long getId();

	String getUsername();

	String getEmail();

	String getRoles();

	LocalDateTime getCreatedAt();

	Long getReadCount();

	Long getBookmarkCount();

	Long getCompletedCount();

	Long getReviewsCount();

	LocalDateTime getLastActivity();

	String getRegisterType();

	default OffsetDateTime toOffsetDateTime(LocalDateTime time) {
		return Objects.isNull(time) ? null :
				time.atZone(TimeZone.getDefault().toZoneId()).toOffsetDateTime();
	}

	default List<String> getUserRoles() {
		return Objects.isNull(getRoles()) ? null :
				Arrays.stream(this.getRoles().split(",")).toList();
	}

	default UserInfoDTO getUserInfoDTO() {
		return new UserInfoDTO(
				this.getId(),
				this.getUsername(),
				this.getEmail(),
				toOffsetDateTime(getCreatedAt()),
				getUserRoles(),
				this.getReadCount(),
				this.getBookmarkCount(),
				this.getCompletedCount(),
				this.getReviewsCount(),
				toOffsetDateTime(getLastActivity()),
				this.getRegisterType()
		);
	}

	default UserInfoReadOnlyDTO getReadOnlyUserInfoDTO() {
		return new UserInfoReadOnlyDTO(
				this.getId(),
				this.getUsername(),
				toOffsetDateTime(getCreatedAt()),
				getUserRoles(),
				this.getReadCount(),
				this.getBookmarkCount(),
				this.getCompletedCount(),
				this.getReviewsCount(),
				toOffsetDateTime(getLastActivity())
		);
	}
}
