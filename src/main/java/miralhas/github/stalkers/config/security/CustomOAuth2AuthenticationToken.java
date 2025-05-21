package miralhas.github.stalkers.config.security;

import miralhas.github.stalkers.domain.exception.BusinessException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class CustomOAuth2AuthenticationToken extends AbstractAuthenticationToken {

	public CustomOAuth2AuthenticationToken() {
		super(AuthorityUtils.createAuthorityList("ROLE_OAUTH2"));
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		throw new BusinessException("OAuth2 Authentication Token is immutable");
	}
}
