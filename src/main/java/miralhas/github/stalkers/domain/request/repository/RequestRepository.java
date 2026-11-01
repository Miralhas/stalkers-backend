package miralhas.github.stalkers.domain.request.repository;

import miralhas.github.stalkers.domain.request.model.BaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequestRepository extends JpaRepository<BaseRequest, Long>, JpaSpecificationExecutor<BaseRequest> {
}