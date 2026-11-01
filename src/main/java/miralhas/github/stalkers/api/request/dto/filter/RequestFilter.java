package miralhas.github.stalkers.api.request.dto.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.config.validation.EnumPattern;
import miralhas.github.stalkers.domain.request.model.BaseRequest;
import miralhas.github.stalkers.domain.request.model.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import static miralhas.github.stalkers.infrastructure.request.specs.RequestSpec.statusEquals;
import static miralhas.github.stalkers.infrastructure.request.specs.RequestSpec.requestTypeEquals;


public record RequestFilter(
		@EnumPattern(enumClass = Status.class)
		String status,

		@EnumPattern(enumClass= TypeEnum.class)
		String type
) {

	@Getter
	@RequiredArgsConstructor
	public enum TypeEnum {
		CHAPTER(BaseRequest.CHAPTER_REQUEST),
		NOVEL(BaseRequest.NOVEL_REQUEST),
		FIX_CHAPTER(BaseRequest.FIX_CHAPTER_REQUEST);
		private final String requestType;
	}

	public Specification<BaseRequest> toSpecification() {
		return statusEquals(status).and(requestTypeEquals(type));
	}

}
