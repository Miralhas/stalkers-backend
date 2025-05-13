package miralhas.github.stalkers.domain.model.notification;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miralhas.github.stalkers.domain.model.notification.enums.NotificationType;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("NEW_VOTE")
public class UpvoteNotification extends Notification implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public void setType() {
		super.setType(NotificationType.NEW_VOTE);
	}

}
