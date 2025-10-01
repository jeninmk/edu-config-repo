package org.com.education.progress.progressservice.repository;



import org.com.education.progress.progressservice.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
}
