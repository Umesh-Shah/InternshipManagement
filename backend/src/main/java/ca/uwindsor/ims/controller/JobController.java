package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.JobRequest;
import ca.uwindsor.ims.entity.Job;
import ca.uwindsor.ims.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
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
    public Job create(@RequestBody JobRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Job> update(@PathVariable Integer id, @RequestBody JobRequest request) {
        return service.update(id, request).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.delete(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
