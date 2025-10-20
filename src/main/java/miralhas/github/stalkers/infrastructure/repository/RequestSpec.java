package miralhas.github.stalkers.infrastructure.repository;

import lombok.experimental.UtilityClass;
import miralhas.github.stalkers.api.dto.filter.RequestFilter;
import miralhas.github.stalkers.domain.model.requests.BaseRequest;
import org.springframework.data.jpa.domain.Specification;

import static org.springframework.util.StringUtils.hasText;

@UtilityClass
public class RequestSpec {

	public static Specification<BaseRequest> statusEquals(String status) {
		return (root, query, builder) -> {
			if (!hasText(status)) return null;
			return builder.equal(root.get("status"), status);
		};
	}

	public static Specification<BaseRequest> requestTypeEquals(String type) {
		return (root, query, builder) -> {
			if (!hasText(type)) return null;
			var requestType = RequestFilter.TypeEnum.valueOf(type).getRequestType();
			return builder.equal(root.get("requestType"), requestType);
		};
	}

}
