package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.requests.BaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<BaseRequest, Long> {
}