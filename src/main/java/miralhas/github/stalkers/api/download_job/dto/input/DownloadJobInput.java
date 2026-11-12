package miralhas.github.stalkers.api.download_job.dto.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DownloadJobInput(
		@NotBlank
		String slug,

		@Min(0)
		Integer start,

		@Min(0)
		Integer end,

		@Size(min = 1)
		List<@Positive Integer> exclude
) {
}
