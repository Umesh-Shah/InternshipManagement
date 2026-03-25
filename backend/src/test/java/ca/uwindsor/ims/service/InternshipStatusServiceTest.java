package ca.uwindsor.ims.service;

import ca.uwindsor.ims.Constants;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InternshipStatusServiceTest {

    @Mock StudentInternshipRepository studentInternshipRepo;
    @Mock StudentInfoRepository studentInfoRepo;
    @Mock CompanyRepository companyRepo;
    @Mock JobRepository jobRepo;

    @InjectMocks InternshipStatusService service;

    @Test
    void assign_createsNewRecord_andReturnsResponse() {
        InternshipStatusRequest req = new InternshipStatusRequest(1001, 10, 20, 1, "CO-OP", "Active");

        when(studentInternshipRepo.findByStudentIdAndJobId(1001, 20)).thenReturn(Optional.empty());
        StudentInternship saved = new StudentInternship();
        saved.setStudentInternshipId(1);
        saved.setStudentId(1001);
        saved.setCompanyId(10);
        saved.setJobId(20);
        saved.setInternshipId(1);
        saved.setInternshipType("CO-OP");
        saved.setInternshipStatus("Active");
        when(studentInternshipRepo.save(any())).thenReturn(saved);
        when(studentInfoRepo.findByStudentId(1001)).thenReturn(Optional.empty());
        when(companyRepo.findById(10)).thenReturn(Optional.empty());
        when(jobRepo.findById(20)).thenReturn(Optional.empty());

        InternshipStatusResponse response = service.assign(req);

        assertThat(response.studentId()).isEqualTo(1001);
        assertThat(response.companyId()).isEqualTo(10);
        assertThat(response.jobId()).isEqualTo(20);
        assertThat(response.internshipType()).isEqualTo("CO-OP");
        verify(studentInternshipRepo).save(any());
    }

    @Test
    void assign_updatesStudentInfoWhenPresent() {
        InternshipStatusRequest req = new InternshipStatusRequest(1001, 10, 20, 1, "CO-OP", "Active");

        StudentInternship saved = new StudentInternship();
        saved.setStudentId(1001);
        saved.setCompanyId(10);
        saved.setJobId(20);
        when(studentInternshipRepo.findByStudentIdAndJobId(1001, 20)).thenReturn(Optional.empty());
        when(studentInternshipRepo.save(any())).thenReturn(saved);

        StudentInfo info = new StudentInfo();
        info.setStudentId(1001);
        info.setStudentStatus(Constants.STUDENT_STATUS_ACTIVE);
        when(studentInfoRepo.findByStudentId(1001)).thenReturn(Optional.of(info));
        when(studentInfoRepo.save(any())).thenReturn(info);
        when(companyRepo.findById(10)).thenReturn(Optional.empty());
        when(jobRepo.findById(20)).thenReturn(Optional.empty());

        service.assign(req);

        assertThat(info.getStudentStatus()).isEqualTo(Constants.STUDENT_STATUS_HIRED);
        verify(studentInfoRepo).save(info);
    }

    @Test
    void getByStudent_returnsMatchingRecords() {
        StudentInternship si = new StudentInternship();
        si.setStudentId(1001);
        si.setCompanyId(10);
        si.setJobId(20);

        Company company = new Company();
        company.setCompanyName("Acme Corp");
        Job job = new Job();
        job.setJobPosition("Developer");
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setFname("Alice");
        studentInfo.setLname("Smith");
        studentInfo.setStudentStatus(Constants.STUDENT_STATUS_ACTIVE);

        when(studentInternshipRepo.findByStudentId(1001)).thenReturn(List.of(si));
        when(companyRepo.findById(10)).thenReturn(Optional.of(company));
        when(jobRepo.findById(20)).thenReturn(Optional.of(job));
        when(studentInfoRepo.findByStudentId(1001)).thenReturn(Optional.of(studentInfo));

        List<InternshipStatusResponse> result = service.getByStudent(1001);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).companyName()).isEqualTo("Acme Corp");
        assertThat(result.get(0).jobPosition()).isEqualTo("Developer");
        assertThat(result.get(0).studentName()).isEqualTo("Alice Smith");
    }
}
