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

import java.util.List;
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

    @Test
    void findAll_delegatesToRepo() {
        Skill s = new Skill();
        s.setSkillName("Java");
        when(repo.findAll()).thenReturn(List.of(s));

        assertThat(service.findAll()).hasSize(1).first()
                .extracting(Skill::getSkillName).isEqualTo("Java");
    }

    @Test
    void findById_existingId_returnsSkill() {
        Skill s = new Skill();
        s.setSkillName("Python");
        when(repo.findById(1)).thenReturn(Optional.of(s));

        assertThat(service.findById(1)).isPresent()
                .hasValueSatisfying(sk -> assertThat(sk.getSkillName()).isEqualTo("Python"));
    }

    @Test
    void findById_unknownId_returnsEmpty() {
        when(repo.findById(9999)).thenReturn(Optional.empty());

        assertThat(service.findById(9999)).isEmpty();
    }

    @Test
    void update_existingSkill_updatesFieldsAndReturns() {
        Skill existing = new Skill();
        existing.setSkillName("OldName");
        existing.setSkillType("OldType");
        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Skill> result = service.update(1, new SkillRequest("NewName", "NewType"));

        assertThat(result).isPresent();
        assertThat(result.get().getSkillName()).isEqualTo("NewName");
        assertThat(result.get().getSkillType()).isEqualTo("NewType");
    }
}
