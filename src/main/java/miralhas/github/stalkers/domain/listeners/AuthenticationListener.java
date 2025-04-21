package miralhas.github.stalkers.domain.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.domain.service.interfaces.SendEmailService;
import miralhas.github.stalkers.domain.service.interfaces.SendEmailService.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
	public void passwordResetListener(UserDTO user) {
		log.info("Password reset request received: {}", user);

		var message = Message.builder()
				.body("reset-password-token-created")
				.recipient(user.email())
				.subject("%s - Password Reset".formatted(user.username()))
				.model("user", user)
				.model("token", UUID.randomUUID().toString())
				.build();

		sendEmailService.send(message);
	}

	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = "stalkers", type = "topic"),
			value = @Queue("queue.user.created"),
			key = "rk.user.created"
	))
	public void userCreatedListener(UserDTO user) {

	}
}
