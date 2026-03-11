package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.JobApplicationRequest;
import ca.uwindsor.ims.dto.JobApplicationResponse;
import ca.uwindsor.ims.entity.Job;
import ca.uwindsor.ims.entity.StudentInfo;
import ca.uwindsor.ims.entity.StudentJobMapping;
import ca.uwindsor.ims.repository.CompanyRepository;
import ca.uwindsor.ims.repository.JobRepository;
import ca.uwindsor.ims.repository.StudentInfoRepository;
import ca.uwindsor.ims.repository.StudentJobMappingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JobApplicationService {

    private final StudentJobMappingRepository mappingRepo;
    private final JobRepository jobRepo;
    private final CompanyRepository companyRepo;
    private final StudentInfoRepository studentRepo;

    public JobApplicationService(
            StudentJobMappingRepository mappingRepo,
            JobRepository jobRepo,
            CompanyRepository companyRepo,
            StudentInfoRepository studentRepo) {
        this.mappingRepo = mappingRepo;
        this.jobRepo = jobRepo;
        this.companyRepo = companyRepo;
        this.studentRepo = studentRepo;
    }

    // ── Student: mark interest ──────────────────────────────────────────────────

    @Transactional
    public JobApplicationResponse apply(JobApplicationRequest req) {
        // Idempotent: if already applied, return existing record
        return mappingRepo.findByStudentIdAndJobId(req.studentId(), req.jobId())
                .map(this::toResponse)
                .orElseGet(() -> {
                    StudentJobMapping m = new StudentJobMapping();
                    m.setStudentId(req.studentId());
                    m.setJobId(req.jobId());
                    m.setFlag("N");
                    return toResponse(mappingRepo.save(m));
                });
    }

    // ── Student: list own applications ─────────────────────────────────────────

    public List<JobApplicationResponse> getByStudent(Integer studentId) {
        return mappingRepo.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Admin: list pending applications ───────────────────────────────────────

    public List<JobApplicationResponse> getPending() {
        return mappingRepo.findByFlag("N").stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Admin: list approved students for a job ─────────────────────────────────

    public List<JobApplicationResponse> getApprovedByJob(Integer jobId) {
        return mappingRepo.findByJobId(jobId).stream()
                .filter(m -> "A".equals(m.getFlag()))
                .map(this::toResponse)
                .toList();
    }

    // ── Admin: approve ─────────────────────────────────────────────────────────

    @Transactional
    public JobApplicationResponse approve(Integer studentJobId) {
        StudentJobMapping m = mappingRepo.findById(studentJobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        m.setFlag("A");
        return toResponse(mappingRepo.save(m));
    }

    // ── Private helper ─────────────────────────────────────────────────────────

    private JobApplicationResponse toResponse(StudentJobMapping m) {
        Job job = jobRepo.findById(m.getJobId()).orElse(null);
        String jobPosition = job != null ? job.getJobPosition() : null;
        Integer companyId = job != null ? job.getCompanyId() : null;
        String companyName = null;
        if (companyId != null) {
            companyName = companyRepo.findById(companyId)
                    .map(c -> c.getCompanyName())
                    .orElse(null);
        }
        StudentInfo student = studentRepo.findByStudentId(m.getStudentId()).orElse(null);
        String studentName = student != null
                ? (student.getFname() + " " + student.getLname()).trim()
                : String.valueOf(m.getStudentId());

        return new JobApplicationResponse(
                m.getStudentJobId(),
                m.getJobId(),
                jobPosition,
                companyId,
                companyName,
                m.getStudentId(),
                studentName,
                m.getFlag()
        );
    }
}
