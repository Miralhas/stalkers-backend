package miralhas.github.stalkers.api.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import miralhas.github.stalkers.api.dto.UserDTO;
import miralhas.github.stalkers.api.dto.UserInfoDTO;
import miralhas.github.stalkers.api.dto.input.UpdateUserInput;
import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "User Controller", description = "Controller Responsible for user operations")
public interface UserControllerSwagger {

	@Operation(summary = "Find all users")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Users array", content = @Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
	})
	Page<UserInfoDTO> getAllUsers();

	@Operation(summary = "Update user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User updated", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class))),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
	})
	UserDTO updateUser(@RequestBody @Valid UpdateUserInput updateUserInput, JwtAuthenticationToken token);
}
