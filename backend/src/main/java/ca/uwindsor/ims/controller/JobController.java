package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.JobRequest;
import ca.uwindsor.ims.entity.Job;
import ca.uwindsor.ims.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    private final JobService service;

    public JobController(JobService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public List<Job> getAll(@RequestParam(required = false) Integer companyId) {
        return companyId != null ? service.findByCompanyId(companyId) : service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<Job> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Job create(@RequestBody JobRequest request) {
        Job created = service.create(request);
        log.info("Job posted: id={}, position={}, companyId={}", created.getJobId(), created.getJobPosition(), created.getCompanyId());
        return created;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Job> update(@PathVariable Integer id, @RequestBody JobRequest request) {
        ResponseEntity<Job> response = service.update(id, request).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Job updated: id={}", id);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            log.info("Job deleted: id={}", id);
        }
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
