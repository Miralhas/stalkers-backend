package miralhas.github.stalkers.domain.shared.event;

public record SendTransactionalMessageEvent(Object message, String routingKey, String exchange) {}
