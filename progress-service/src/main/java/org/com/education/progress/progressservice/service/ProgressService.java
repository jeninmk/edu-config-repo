package org.com.education.progress.progressservice.service;



import org.com.education.progress.progressservice.entity.Progress;
import org.com.education.progress.progressservice.repository.ProgressRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    private final ProgressRepository repository;

    public ProgressService(ProgressRepository repository) {
        this.repository = repository;
    }

    public List<Progress> getAllProgress() {
        return repository.findAll();
    }

    public Optional<Progress> getProgressById(Long id) {
        System.out.println("Fetching progress with id: " + id);
        return repository.findById(id);
    }


    public Progress createProgress(Progress progress) {
        progress.setLastUpdated(LocalDateTime.now());
        return repository.save(progress);
    }

    public Progress updateProgress(Long id, Progress progress) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCourseId(progress.getCourseId());
                    existing.setStudentId(progress.getStudentId());
                    existing.setProgressPercent(progress.getProgressPercent());
                    existing.setLastUpdated(LocalDateTime.now());
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("Progress not found with id " + id));
    }

    public void deleteProgress(Long id) {
        repository.deleteById(id);
    }
}
