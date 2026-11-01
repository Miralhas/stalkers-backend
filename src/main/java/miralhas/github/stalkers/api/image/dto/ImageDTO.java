package miralhas.github.stalkers.api.image.dto;

public record ImageDTO(
		String fileName,
		String description,
		String contentType,
		Long size
) {
}
