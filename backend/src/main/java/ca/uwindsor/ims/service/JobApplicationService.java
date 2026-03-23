package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.JobApplicationRequest;
import ca.uwindsor.ims.dto.JobApplicationResponse;
import ca.uwindsor.ims.entity.StudentJobMapping;
import ca.uwindsor.ims.repository.StudentJobMappingRepository;
import ca.uwindsor.ims.repository.StudentJobMappingRepository.JobApplicationProjection;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JobApplicationService {

    private final StudentJobMappingRepository mappingRepo;

    public JobApplicationService(StudentJobMappingRepository mappingRepo) {
        this.mappingRepo = mappingRepo;
    }

    // ── Student: mark interest ──────────────────────────────────────────────────

    @Transactional
    public JobApplicationResponse apply(JobApplicationRequest req) {
        return mappingRepo.findByStudentIdAndJobId(req.studentId(), req.jobId())
                .map(this::toResponseFromEntity)
                .orElseGet(() -> {
                    StudentJobMapping m = new StudentJobMapping();
                    m.setStudentId(req.studentId());
                    m.setJobId(req.jobId());
                    m.setFlag("N");
                    return toResponseFromEntity(mappingRepo.save(m));
                });
    }

    // ── Student: list own applications ─────────────────────────────────────────

    public List<JobApplicationResponse> getByStudent(Integer studentId) {
        return mappingRepo.findApplications(null, studentId, null).stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Admin: list pending (flag='N') applications ─────────────────────────────

    public List<JobApplicationResponse> getPending() {
        return mappingRepo.findApplications("N", null, null).stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Admin: approve an application ──────────────────────────────────────────

    @Transactional
    public JobApplicationResponse approve(Integer studentJobId) {
        StudentJobMapping m = mappingRepo.findById(studentJobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        m.setFlag("A");
        return toResponseFromEntity(mappingRepo.save(m));
    }

    // ── Admin: approved students for a specific job ─────────────────────────────

    public List<JobApplicationResponse> getApprovedByJob(Integer jobId) {
        return mappingRepo.findApplications("A", null, jobId).stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private JobApplicationResponse toResponse(JobApplicationProjection p) {
        return new JobApplicationResponse(
                p.getStudentJobId(),
                p.getJobId(),
                p.getJobPosition(),
                p.getCompanyId(),
                p.getCompanyName(),
                p.getStudentId(),
                p.getStudentName(),
                p.getFlag());
    }

    /** Used when we only have a freshly saved entity (no join data yet). */
    private JobApplicationResponse toResponseFromEntity(StudentJobMapping m) {
        return new JobApplicationResponse(
                m.getStudentJobId(),
                m.getJobId(),
                null,
                null,
                null,
                m.getStudentId(),
                String.valueOf(m.getStudentId()),
                m.getFlag());
    }
}
