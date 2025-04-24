package miralhas.github.stalkers.api.dto;

public record ImageDTO(
		String fileName,
		String description,
		String contentType,
		Long size
) {
}
