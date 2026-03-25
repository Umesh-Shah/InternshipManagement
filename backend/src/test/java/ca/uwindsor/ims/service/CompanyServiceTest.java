package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.CompanyRequest;
import ca.uwindsor.ims.entity.Company;
import ca.uwindsor.ims.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock CompanyRepository repo;
    @InjectMocks CompanyService service;

    @Test
    void create_mapsRequestToEntity() {
        CompanyRequest req = new CompanyRequest(
                "Acme Corp", "123 Main St", "Windsor", "N9A 1A1",
                "Canada", "John", "Doe", "HR", "555-0100",
                "hr@acme.com", "https://acme.com", "Some notes");
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.create(req);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(repo).save(captor.capture());
        Company saved = captor.getValue();
        assertThat(saved.getCompanyName()).isEqualTo("Acme Corp");
        assertThat(saved.getCity()).isEqualTo("Windsor");
        assertThat(saved.getCountry()).isEqualTo("Canada");
        assertThat(saved.getEmail()).isEqualTo("hr@acme.com");
        assertThat(saved.getContactPersonFname()).isEqualTo("John");
        assertThat(saved.getNotes()).isEqualTo("Some notes");
    }

    @Test
    void update_existingCompany_appliesChanges() {
        Company existing = new Company();
        existing.setCompanyId(10);
        existing.setCompanyName("Old Name");
        when(repo.findById(10)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CompanyRequest req = new CompanyRequest(
                "New Name", null, "Toronto", null, null,
                null, null, null, null, null, null, null);
        Optional<Company> result = service.update(10, req);

        assertThat(result).isPresent();
        assertThat(result.get().getCompanyName()).isEqualTo("New Name");
        assertThat(result.get().getCity()).isEqualTo("Toronto");
    }

    @Test
    void update_unknownCompany_returnsEmpty() {
        when(repo.findById(9999)).thenReturn(Optional.empty());

        Optional<Company> result = service.update(9999, new CompanyRequest(
                "X", null, null, null, null, null, null, null, null, null, null, null));

        assertThat(result).isEmpty();
    }

    @Test
    void delete_existingCompany_returnsTrue() {
        when(repo.existsById(10)).thenReturn(true);

        boolean result = service.delete(10);

        assertThat(result).isTrue();
        verify(repo).deleteById(10);
    }

    @Test
    void delete_unknownCompany_returnsFalse() {
        when(repo.existsById(9999)).thenReturn(false);

        assertThat(service.delete(9999)).isFalse();
    }
}
