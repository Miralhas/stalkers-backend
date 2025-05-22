package miralhas.github.stalkers.api.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import miralhas.github.stalkers.api.dto.AuthenticationDTO;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.input.*;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Authentication Controller", description = "Controller responsible for user authentication operations")
public interface AuthenticationControllerSwagger {

	@Operation(summary = "Create new user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
	})
	UserDTO signUp(@RequestBody @Valid CreateUserInput createUserInput);

	@Operation(summary = "Authenticate User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User authenticated", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationDTO.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "401", description = "Bad credentials", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "404", description = "Username not found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
	})
	AuthenticationDTO signIn(@RequestBody @Valid SigninInput signinInput);

	@Operation(summary = "Forgot password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Forgot password email sent"),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
	})
	void createPasswordResetToken(@RequestBody @Valid ForgotPasswordInput forgotPasswordInput);

	@Operation(summary = "Reset account password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Password reseted"),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "404", description = "Reset password token not found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
	})
	void resetPassword(
			@PathVariable String token,
			@RequestBody @Valid ChangePasswordInput changePasswordInput
	);

//	@Operation(summary = "Refresh token")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "Token refreshed", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationDTO.class))),
//			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
//			@ApiResponse(responseCode = "404", description = "Refresh token not found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
//			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
//	})
//	AuthenticationDTO refreshToken(@RequestBody @Valid RefreshTokenInput refreshTokenInput);
}
