package miralhas.github.stalkers.api.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.With;
import miralhas.github.stalkers.config.validation.FileContentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@With
public record ImageInput(
		@NotNull
		@FileContentType
		@Schema(description = "Image file")
		MultipartFile file,

		String description
) {
	public InputStream fileInputStream() throws IOException {
		return file.getInputStream();
	}
}
