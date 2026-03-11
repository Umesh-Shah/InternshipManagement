package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.InternshipStatusRequest;
import ca.uwindsor.ims.dto.InternshipStatusResponse;
import ca.uwindsor.ims.entity.Company;
import ca.uwindsor.ims.entity.Job;
import ca.uwindsor.ims.entity.StudentInfo;
import ca.uwindsor.ims.entity.StudentInternship;
import ca.uwindsor.ims.repository.CompanyRepository;
import ca.uwindsor.ims.repository.JobRepository;
import ca.uwindsor.ims.repository.StudentInfoRepository;
import ca.uwindsor.ims.repository.StudentInternshipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipStatusService {

    private final StudentInternshipRepository studentInternshipRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    public InternshipStatusService(
            StudentInternshipRepository studentInternshipRepository,
            StudentInfoRepository studentInfoRepository,
            CompanyRepository companyRepository,
            JobRepository jobRepository) {
        this.studentInternshipRepository = studentInternshipRepository;
        this.studentInfoRepository = studentInfoRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }

    public InternshipStatusResponse assign(InternshipStatusRequest req) {
        StudentInternship si = new StudentInternship();
        si.setStudentId(req.studentId());
        si.setCompanyId(req.companyId());
        si.setJobId(req.jobId());
        si.setInternshipId(req.internshipId());
        si.setInternshipType(req.internshipType());
        si.setInternshipStatus(req.internshipStatus());

        StudentInternship saved = studentInternshipRepository.save(si);

        studentInfoRepository.findByStudentId(req.studentId()).ifPresent(info -> {
            info.setStudentStatus("Hired");
            info.setInternshipStatus(req.internshipType());
            studentInfoRepository.save(info);
        });

        return toResponse(saved);
    }

    public List<InternshipStatusResponse> getAll() {
        return studentInternshipRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InternshipStatusResponse> getByStudent(Integer studentId) {
        return studentInternshipRepository.findByStudentId(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private InternshipStatusResponse toResponse(StudentInternship si) {
        String companyName = companyRepository.findById(si.getCompanyId())
                .map(Company::getCompanyName)
                .orElse(null);

        String jobPosition = jobRepository.findById(si.getJobId())
                .map(Job::getJobPosition)
                .orElse(null);

        String studentName = null;
        String studentStatus = null;
        if (si.getStudentId() != null) {
            java.util.Optional<StudentInfo> infoOpt = studentInfoRepository.findByStudentId(si.getStudentId());
            if (infoOpt.isPresent()) {
                StudentInfo info = infoOpt.get();
                studentName = (info.getFname() != null ? info.getFname() : "")
                        + " " + (info.getLname() != null ? info.getLname() : "");
                studentName = studentName.trim();
                studentStatus = info.getStudentStatus();
            }
        }

        return new InternshipStatusResponse(
                si.getStudentInternshipId(),
                si.getStudentId(),
                studentName,
                si.getCompanyId(),
                companyName,
                si.getJobId(),
                jobPosition,
                si.getInternshipId(),
                si.getInternshipType(),
                si.getInternshipStatus(),
                studentStatus
        );
    }
}
