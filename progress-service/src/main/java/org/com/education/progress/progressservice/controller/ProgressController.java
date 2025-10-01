package org.com.education.progress.progressservice.controller;



import org.com.education.progress.progressservice.entity.Progress;
import org.com.education.progress.progressservice.service.ProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService service;
    private final ProgressService progressService;

    public ProgressController(ProgressService service, ProgressService progressService) {
        this.service = service;
        this.progressService = progressService;
    }

    @GetMapping
    public List<Progress> getAllProgress() {
        return service.getAllProgress();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Progress> getProgressById(@PathVariable("id") Long id) {
        try {
            return service.getProgressById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public Progress createProgress(@RequestBody Progress progress) {
        return service.createProgress(progress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Progress> updateProgress(@PathVariable("id") Long id, @RequestBody Progress progress) {
        try {
            return ResponseEntity.ok(service.updateProgress(id, progress));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable("id") Long id) {
        if (service.getProgressById(id).isPresent()) {
            service.deleteProgress(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
