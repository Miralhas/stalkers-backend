package miralhas.github.stalkers.api.download_job.mapper;

import miralhas.github.stalkers.api.download_job.dto.DownloadJobDTO;
import miralhas.github.stalkers.api.download_job.dto.input.DownloadJobInput;
import miralhas.github.stalkers.domain.download_job.model.DownloadJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = IGNORE,
		nullValueCheckStrategy = ALWAYS
)
public interface DownloadJobMapper {
	DownloadJobDTO toResponse(DownloadJob job);

	@Mapping(target = "exclude", qualifiedByName = "excludeNumbers")
	DownloadJob fromInput(DownloadJobInput input);

	@Named("excludeNumbers")
	default String excludeToString(List<Integer> exclude) {
		return String.join(";", exclude.stream().map(Object::toString).toList());
	}
}
