package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.JobRequest;
import ca.uwindsor.ims.entity.Job;
import ca.uwindsor.ims.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class JobServiceTest {

    @Mock JobRepository repo;
    @InjectMocks JobService service;

    @Test
    void create_setsCompanyId() {
        JobRequest req = new JobRequest("Software Dev", "desc", "reqs", 60000, 10, "resp", "Java", "CO-OP");
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.create(req);

        ArgumentCaptor<Job> captor = ArgumentCaptor.forClass(Job.class);
        verify(repo).save(captor.capture());
        Job saved = captor.getValue();
        assertThat(saved.getCompanyId()).isEqualTo(10);
        assertThat(saved.getJobPosition()).isEqualTo("Software Dev");
        assertThat(saved.getInternshipType()).isEqualTo("CO-OP");
    }

    @Test
    void update_existingJob_appliesChanges() {
        Job existing = new Job();
        existing.setJobId(1);
        existing.setJobPosition("Old Title");
        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Job> result = service.update(1,
                new JobRequest("New Title", null, null, null, 10, null, null, null));

        assertThat(result).isPresent();
        assertThat(result.get().getJobPosition()).isEqualTo("New Title");
    }

    @Test
    void delete_existingJob_returnsTrue() {
        when(repo.existsById(1)).thenReturn(true);

        assertThat(service.delete(1)).isTrue();
        verify(repo).deleteById(1);
    }

    @Test
    void findByCompanyId_delegatesToRepo() {
        Job j = new Job();
        j.setCompanyId(10);
        when(repo.findByCompanyId(10)).thenReturn(List.of(j));

        List<Job> result = service.findByCompanyId(10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompanyId()).isEqualTo(10);
        verify(repo).findByCompanyId(10);
    }
}
