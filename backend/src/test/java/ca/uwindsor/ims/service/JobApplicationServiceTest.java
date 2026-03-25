package ca.uwindsor.ims.service;

import ca.uwindsor.ims.Constants;
import ca.uwindsor.ims.dto.JobApplicationRequest;
import ca.uwindsor.ims.dto.JobApplicationResponse;
import ca.uwindsor.ims.entity.StudentJobMapping;
import ca.uwindsor.ims.repository.StudentJobMappingRepository;
import ca.uwindsor.ims.repository.StudentJobMappingRepository.JobApplicationProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock StudentJobMappingRepository mappingRepo;
    @InjectMocks JobApplicationService service;

    @Test
    void apply_newApplication_createsMappingWithPendingFlag() {
        when(mappingRepo.findByStudentIdAndJobId(1001, 20)).thenReturn(Optional.empty());
        StudentJobMapping saved = new StudentJobMapping();
        saved.setStudentJobId(1);
        saved.setStudentId(1001);
        saved.setJobId(20);
        saved.setFlag(Constants.FLAG_PENDING);
        when(mappingRepo.save(any())).thenReturn(saved);

        JobApplicationResponse response = service.apply(new JobApplicationRequest(1001, 20));

        assertThat(response.studentId()).isEqualTo(1001);
        assertThat(response.jobId()).isEqualTo(20);
        assertThat(response.flag()).isEqualTo(Constants.FLAG_PENDING);
    }

    @Test
    void apply_duplicateApplication_returnsExisting() {
        StudentJobMapping existing = new StudentJobMapping();
        existing.setStudentJobId(5);
        existing.setStudentId(1001);
        existing.setJobId(20);
        existing.setFlag(Constants.FLAG_ACTIVE);
        when(mappingRepo.findByStudentIdAndJobId(1001, 20)).thenReturn(Optional.of(existing));

        JobApplicationResponse response = service.apply(new JobApplicationRequest(1001, 20));

        assertThat(response.studentJobId()).isEqualTo(5);
        assertThat(response.flag()).isEqualTo(Constants.FLAG_ACTIVE);
    }

    @Test
    void approve_setsFlag_A() {
        StudentJobMapping m = new StudentJobMapping();
        m.setStudentJobId(5);
        m.setStudentId(1001);
        m.setJobId(20);
        m.setFlag(Constants.FLAG_PENDING);
        when(mappingRepo.findById(5)).thenReturn(Optional.of(m));
        when(mappingRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        JobApplicationResponse response = service.approve(5);

        assertThat(response.flag()).isEqualTo(Constants.FLAG_ACTIVE);
    }

    @Test
    void approve_unknownApplication_throws404() {
        when(mappingRepo.findById(9999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approve(9999))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void getPending_returnsOnlyFlagN() {
        JobApplicationProjection proj = mock(JobApplicationProjection.class);
        when(proj.getStudentJobId()).thenReturn(1);
        when(proj.getJobId()).thenReturn(20);
        when(proj.getJobPosition()).thenReturn("Dev");
        when(proj.getCompanyId()).thenReturn(10);
        when(proj.getCompanyName()).thenReturn("Acme");
        when(proj.getStudentId()).thenReturn(1001);
        when(proj.getStudentName()).thenReturn("Alice Smith");
        when(proj.getFlag()).thenReturn(Constants.FLAG_PENDING);
        when(mappingRepo.findApplications(Constants.FLAG_PENDING, null, null)).thenReturn(List.of(proj));

        List<JobApplicationResponse> result = service.getPending();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).flag()).isEqualTo(Constants.FLAG_PENDING);
        verify(mappingRepo).findApplications(Constants.FLAG_PENDING, null, null);
    }
}
