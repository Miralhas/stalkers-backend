package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.AuthenticationDTO;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.input.*;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.config.properties_metadata.TokenPropertiesConfig;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final TokenPropertiesConfig tokenPropertiesConfig;

	private final UserMapper userMapper;
	private final UserService userService;
	private final AuthenticationService authenticationService;
	private final RefreshTokenService refreshTokenService;
	private final TokenService tokenService;
	private final PasswordResetService passwordResetService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO signUp(@RequestBody @Valid CreateUserInput createUserInput) {
		User user = userMapper.fromInput(createUserInput);
		user = userService.create(user);
		return userMapper.toResponse(user);
	}

	@PostMapping("/signin")
	@ResponseStatus(HttpStatus.OK)
	public AuthenticationDTO login(@RequestBody @Valid SigninInput signinInput) {
		return authenticationService.authenticate(signinInput);
	}

	@PutMapping("/forgotPassword")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void createPasswordResetToken(@RequestBody @Valid ForgotPasswordInput forgotPasswordInput) {
		passwordResetService.createOrUpdateToken(forgotPasswordInput.email());
	}


	@PutMapping("/resetPassword/{token}")
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
				tokenPropertiesConfig.accessToken().expirationTime(),
				tokenPropertiesConfig.refreshToken().expirationTime()
		);
	}
}
