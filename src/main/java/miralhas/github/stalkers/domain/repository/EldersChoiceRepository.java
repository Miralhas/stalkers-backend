package miralhas.github.stalkers.domain.repository;

import miralhas.github.stalkers.domain.model.EldersChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EldersChoiceRepository extends JpaRepository<EldersChoice, Long> {

	@Override
	@Query(value = "from EldersChoice ec left join fetch ec.novel")
	List<EldersChoice> findAll();

	@Query(nativeQuery = true, value = "SELECT IF(" +
			"EXISTS(SELECT 1 FROM elders_choice ec WHERE ec.novel_id = :novelId), 'true', 'false')")
	boolean novelAlreadySelected(Long novelId);
}