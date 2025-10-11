package miralhas.github.stalkers.api.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.filter.ChaptersRange;
import miralhas.github.stalkers.domain.service.DownloadService;
import miralhas.github.stalkers.domain.service.NovelService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/downloads/{novelSlug}")
public class DownloadController {

	private final DownloadService downloadService;
	private final NovelService novelService;

	@GetMapping("/pdf")
	public void downloadNovelPDF(
			@PathVariable String novelSlug, @RequestParam(name = "range") ChaptersRange range,
			HttpServletResponse response
	) throws IOException {
		var novel = novelService.findBySlugOrException(novelSlug);
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + novel.getSlug()+ " c%s-%s".formatted(range.getFirstValue(), range.getSecondValue()) + ".pdf\"");

		response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");

		try (ServletOutputStream out = response.getOutputStream()) {
			downloadService.novelHTMLToPDF(novel, range, out);
			out.flush();
		}
	}
}
