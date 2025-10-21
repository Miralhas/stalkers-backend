package miralhas.github.stalkers.api.dto.input;

public record UpdateAnnouncementInput(
		String body,
		String title,
		boolean pinned
) {
}
