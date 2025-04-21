package miralhas.github.stalkers.infrastructure.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import miralhas.github.stalkers.domain.service.interfaces.SendEmailService;
import miralhas.github.stalkers.infrastructure.exception.EmailException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements SendEmailService {

	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;

	@Override
	public void send(Message message) {
		try {
			log.info("Sending email to: {}", message.getRecipients().toString());

			var emailMessage = getMimeMessage(message);
			javaMailSender.send(emailMessage);

			log.info("Email sent successfully to: {}", message.getRecipients().toString());
		} catch (Exception e) {
			throw new EmailException("Não foi possível enviar o e-mail", e);
		}
	}

	private MimeMessage getMimeMessage(Message message) throws MessagingException {
		var recipients = message.getRecipients().toArray(new String[]{});
		String body = processTemplate(message);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		helper.setText(body, true);
		helper.setSubject(message.getSubject());
		helper.setTo(recipients);
		helper.addInline("logo", new ClassPathResource("static/images/stalkers-logo-resized.png"));
		return mimeMessage;
	}

	private String processTemplate(Message message) {
		var context = new Context();
		context.setVariables(message.getModels());
		return templateEngine.process(message.getBody(), context);
	}
}
