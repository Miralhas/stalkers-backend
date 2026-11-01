package miralhas.github.stalkers.api.announcement.dto.input;

public record UpdateAnnouncementInput(
		String body,
		String title,
		boolean pinned
) {
}
