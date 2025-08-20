package miralhas.github.stalkers.api.dto;

import java.time.LocalDateTime;
import java.util.TimeZone;

public interface UserInfoProjection {
	Long getId();
	String getUsername();
	String getEmail();
	String getRoles();
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
				this.getRoles(),
				this.getReadCount(),
				this.getBookmarkCount(),
				this.getCompletedCount(),
				this.getReviewsCount(),
				this.getLastActivity().atZone(TimeZone.getDefault().toZoneId()).toOffsetDateTime(),
				this.getRegisterType()
		);
	}
}
