package miralhas.github.stalkers.domain.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.filter.ChaptersRange;
import miralhas.github.stalkers.domain.model.novel.Novel;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class DownloadService {

	private final ChapterService chapterService;
	private final TemplateEngine templateEngine;

	public void novelHTMLToPDF(Novel novel, ChaptersRange range, OutputStream outputStream) {
		var chapters = chapterService.getSlimChaptersInRange(range, novel);
		try {
			var context = new Context();
			context.setVariable("novel", novel);
			context.setVariable("chapters", chapters);
			var html = templateEngine.process("novel-pdf-template.html", context);

			try (InputStream htmlStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8))) {
				PdfRendererBuilder builder = new PdfRendererBuilder();
				builder.useFastMode();
				builder.withHtmlContent(new String(htmlStream.readAllBytes(), StandardCharsets.UTF_8), null);
				builder.toStream(outputStream);
				builder.run();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error generating HTML PDF", e);
		}
	}

}
