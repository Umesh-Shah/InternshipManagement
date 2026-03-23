package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.JobApplicationRequest;
import ca.uwindsor.ims.dto.JobApplicationResponse;
import ca.uwindsor.ims.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    /**
     * Student marks interest in a job.
     * Student can only apply on behalf of themselves (enforced via @studentSecurity).
     */
    @PostMapping
    @PreAuthorize("@studentSecurity.canAccess(authentication, #req.studentId())")
    public JobApplicationResponse apply(@Valid @RequestBody JobApplicationRequest req) {
        return service.apply(req);
    }

    /**
     * Student views their own applications.
     */
    @GetMapping
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public List<JobApplicationResponse> getByStudent(@RequestParam Integer studentId) {
        return service.getByStudent(studentId);
    }

    /**
     * Admin: list all pending (flag='N') applications.
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<JobApplicationResponse> getPending() {
        return service.getPending();
    }

    /**
     * Admin: approve an application (flag N → A).
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public JobApplicationResponse approve(@PathVariable Integer id) {
        return service.approve(id);
    }

    /**
     * Admin: list approved students for a specific job.
     */
    @GetMapping("/approved")
    @PreAuthorize("hasRole('ADMIN')")
    public List<JobApplicationResponse> getApprovedByJob(@RequestParam Integer jobId) {
        return service.getApprovedByJob(jobId);
    }
}
