package miralhas.github.stalkers.api.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.auth.dto.AuthenticationDTO;
import miralhas.github.stalkers.api.auth.dto.input.*;
import miralhas.github.stalkers.api.user.dto.UserDTO;
import miralhas.github.stalkers.api.user.mapper.UserMapper;
import miralhas.github.stalkers.api.auth.swagger.AuthenticationControllerSwagger;
import miralhas.github.stalkers.config.properties_metadata.TokenPropertiesConfig;
import miralhas.github.stalkers.domain.auth.service.AuthenticationService;
import miralhas.github.stalkers.domain.auth.service.PasswordResetService;
import miralhas.github.stalkers.domain.auth.service.RefreshTokenService;
import miralhas.github.stalkers.domain.auth.service.TokenService;
import miralhas.github.stalkers.domain.user.model.User;
import miralhas.github.stalkers.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController implements AuthenticationControllerSwagger {

	private final TokenPropertiesConfig tokenPropertiesConfig;

	private final UserMapper userMapper;
	private final UserService userService;
	private final AuthenticationService authenticationService;
	private final RefreshTokenService refreshTokenService;
	private final TokenService tokenService;
	private final PasswordResetService passwordResetService;

	@Override
	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO signUp(@RequestBody @Valid CreateUserInput createUserInput) {
		User user = userMapper.fromInput(createUserInput);
		user = userService.create(user);
		return userMapper.toResponse(user);
	}

	@Override
	@PostMapping("/signin")
	@ResponseStatus(HttpStatus.OK)
	public AuthenticationDTO signIn(@RequestBody @Valid SigninInput signinInput) {
		return authenticationService.authenticate(signinInput);
	}

	@PutMapping("/forgot-password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Override
	public void createPasswordResetToken(@RequestBody @Valid ForgotPasswordInput forgotPasswordInput) {
		passwordResetService.createOrUpdateToken(forgotPasswordInput.email());
	}


	@Override
	@PutMapping("/reset-password/{token}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void resetPassword(
			@PathVariable String token,
			@RequestBody @Valid ChangePasswordInput changePasswordInput
	) {
		passwordResetService.resetPassword(token, changePasswordInput);
	}

	@PostMapping("/refresh-token")
	@ResponseStatus(HttpStatus.OK)
	public AuthenticationDTO refreshToken(@RequestBody @Valid RefreshTokenInput refreshTokenInput) {
		var refreshToken = refreshTokenService.validateRefreshToken(refreshTokenInput.refreshToken());
		var user = userService.findUserByEmailOrException(refreshToken.getUser().getEmail());
		var newAccessToken = tokenService.generateToken(user);
		var newRefreshToken = refreshTokenService.update(refreshToken, user);
		return new AuthenticationDTO(
				newAccessToken.getTokenValue(),
				newRefreshToken.getId().toString(),
				tokenPropertiesConfig.getAccessToken().expirationTime(),
				tokenPropertiesConfig.getRefreshToken().expirationTime()
		);
	}

	@PostMapping("/google")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAnyRole('ADMIN', 'OAUTH2')")
	public AuthenticationDTO googleOAuth2(@RequestBody @Valid GoogleOAuth2Input input) {
		return authenticationService.authenticateOAuth2(input.email());
	}
}
