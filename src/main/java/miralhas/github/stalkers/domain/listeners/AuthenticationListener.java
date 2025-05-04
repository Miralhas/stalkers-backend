package miralhas.github.stalkers.domain.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import miralhas.github.stalkers.api.dto.PasswordResetDTO;
import miralhas.github.stalkers.domain.service.interfaces.SendEmailService;
import miralhas.github.stalkers.domain.service.interfaces.SendEmailService.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class AuthenticationListener {

	private final SendEmailService sendEmailService;

	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = "stalkers", type = "topic"),
			value = @Queue("queue.password_reset"),
			key = "rk.password.reset"
	))
	public void passwordResetListener(PasswordResetDTO passwordReset) {
		log.info("Password reset request received for user: {}", passwordReset.user().username());

		var message = Message.builder()
				.body("reset-password-token-created")
				.recipient(passwordReset.user().email())
				.subject("%s - Password Reset".formatted(passwordReset.user().username()))
				.model("user", passwordReset.user())
				.model("token", passwordReset.token())
				.build();

		sendEmailService.send(message);
	}
}
