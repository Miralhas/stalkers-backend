package miralhas.github.stalkers.api.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
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

	default UserInfoDTO getUserInfoDTO() {
		var lastActivity = Objects.isNull(getLastActivity()) ? null :
				getLastActivity().atZone(TimeZone.getDefault().toZoneId()).toOffsetDateTime();

		var createdAt = Objects.isNull(getCreatedAt()) ? null :
				getCreatedAt().atZone(TimeZone.getDefault().toZoneId()).toOffsetDateTime();

		var roles = Objects.isNull(getRoles()) ? null :
				Arrays.stream(this.getRoles().split(",")).toList();

		return new UserInfoDTO(
				this.getId(),
				this.getUsername(),
				this.getEmail(),
				createdAt,
				roles,
				this.getReadCount(),
				this.getBookmarkCount(),
				this.getCompletedCount(),
				this.getReviewsCount(),
				lastActivity,
				this.getRegisterType()
		);
	}
}
