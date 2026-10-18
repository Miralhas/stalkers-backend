package miralhas.github.stalkers.config.properties_metadata;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client")
public record ClientProperties(
		String domain,
		String oauth2RedirectAfter
) {
}
