package miralhas.github.stalkers.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.AuthenticationDTO;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.input.CreateUserInput;
import miralhas.github.stalkers.api.dto.input.RefreshTokenInput;
import miralhas.github.stalkers.api.dto.input.SigninInput;
import miralhas.github.stalkers.api.dto_mapper.UserMapper;
import miralhas.github.stalkers.domain.model.auth.User;
import miralhas.github.stalkers.domain.service.AuthenticationService;
import miralhas.github.stalkers.domain.service.RefreshTokenService;
import miralhas.github.stalkers.domain.service.TokenService;
import miralhas.github.stalkers.domain.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Value("${access.token.expiration.time}")
	private Long accessTokenExpirationTime;

	@Value("${refresh.token.expiration.time}")
	private Long refreshTokenExpirationTime;

	private final UserMapper userMapper;
	private final UserService userService;
	private final AuthenticationService authenticationService;
	private final RefreshTokenService refreshTokenService;
	private final TokenService tokenService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.OK)
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

	@PostMapping("/refresh-token")
	@ResponseStatus(HttpStatus.OK)
	public AuthenticationDTO refreshToken(@RequestBody @Valid RefreshTokenInput refreshTokenInput) {
		var refreshToken = refreshTokenService.validateRefreshToken(refreshTokenInput.refreshToken());
		var user = userService.findUserByEmailOrException(refreshToken.getUser().getEmail());
		var newAccessToken = tokenService.generateToken(user);
		var newRefreshToken = refreshTokenService.update(refreshToken,user);
		return new AuthenticationDTO(
				newAccessToken.getTokenValue(),
				newRefreshToken.getId().toString(),
				accessTokenExpirationTime,
				refreshTokenExpirationTime
		);
	}
}
