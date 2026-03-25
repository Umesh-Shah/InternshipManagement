package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.SkillRequest;
import ca.uwindsor.ims.entity.Skill;
import ca.uwindsor.ims.repository.SkillRepository;
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
class SkillServiceTest {

    @Mock SkillRepository repo;
    @InjectMocks SkillService service;

    @Test
    void create_setsNameAndType() {
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.create(new SkillRequest("Java", "Programming"));

        ArgumentCaptor<Skill> captor = ArgumentCaptor.forClass(Skill.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().getSkillName()).isEqualTo("Java");
        assertThat(captor.getValue().getSkillType()).isEqualTo("Programming");
    }

    @Test
    void update_unknownSkill_returnsEmpty() {
        when(repo.findById(9999)).thenReturn(Optional.empty());

        Optional<Skill> result = service.update(9999, new SkillRequest("X", "Y"));

        assertThat(result).isEmpty();
    }

    @Test
    void delete_existingSkill_returnsTrue() {
        when(repo.existsById(1)).thenReturn(true);

        assertThat(service.delete(1)).isTrue();
        verify(repo).deleteById(1);
    }

    @Test
    void delete_unknownSkill_returnsFalse() {
        when(repo.existsById(9999)).thenReturn(false);

        assertThat(service.delete(9999)).isFalse();
    }
}
