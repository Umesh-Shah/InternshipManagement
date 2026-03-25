package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.InternshipTypeRequest;
import ca.uwindsor.ims.entity.InternshipType;
import ca.uwindsor.ims.repository.InternshipTypeRepository;
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
class InternshipTypeServiceTest {

    @Mock InternshipTypeRepository repo;
    @InjectMocks InternshipTypeService service;

    @Test
    void create_setsAllFields() {
        InternshipTypeRequest req = new InternshipTypeRequest("CO-OP", "Co-operative education", "Co-op Term");
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.create(req);

        ArgumentCaptor<InternshipType> captor = ArgumentCaptor.forClass(InternshipType.class);
        verify(repo).save(captor.capture());
        InternshipType saved = captor.getValue();
        assertThat(saved.getInternshipType()).isEqualTo("CO-OP");
        assertThat(saved.getDescription()).isEqualTo("Co-operative education");
        assertThat(saved.getInternshipName()).isEqualTo("Co-op Term");
    }

    @Test
    void update_existingType_returnsUpdated() {
        InternshipType existing = new InternshipType();
        existing.setInternshipId(1);
        existing.setInternshipType("OLD");
        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<InternshipType> result = service.update(1,
                new InternshipTypeRequest("NEW", "New desc", "New Name"));

        assertThat(result).isPresent();
        assertThat(result.get().getInternshipType()).isEqualTo("NEW");
        assertThat(result.get().getDescription()).isEqualTo("New desc");
    }

    @Test
    void delete_unknownType_returnsFalse() {
        when(repo.existsById(9999)).thenReturn(false);

        assertThat(service.delete(9999)).isFalse();
    }

    @Test
    void delete_existingType_returnsTrue() {
        when(repo.existsById(1)).thenReturn(true);

        assertThat(service.delete(1)).isTrue();
        verify(repo).deleteById(1);
    }
}
