package miralhas.github.stalkers.domain.download_job.repository;

import miralhas.github.stalkers.domain.download_job.model.DownloadJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadJobRepository extends JpaRepository<DownloadJob, Long> {
}