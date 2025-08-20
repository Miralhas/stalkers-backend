package miralhas.github.stalkers.api.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
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
		return new UserInfoDTO(
				this.getId(),
				this.getUsername(),
				this.getEmail(),
				this.getCreatedAt().atZone(TimeZone.getDefault().toZoneId()).toOffsetDateTime(),
				Arrays.stream(this.getRoles().split(",")).toList(),
				this.getReadCount(),
				this.getBookmarkCount(),
				this.getCompletedCount(),
				this.getReviewsCount(),
				this.getLastActivity().atZone(TimeZone.getDefault().toZoneId()).toOffsetDateTime(),
				this.getRegisterType()
		);
	}
}
