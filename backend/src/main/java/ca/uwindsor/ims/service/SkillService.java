package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.SkillRequest;
import ca.uwindsor.ims.entity.Skill;
import ca.uwindsor.ims.repository.SkillRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository repo;
    public SkillService(SkillRepository repo) { this.repo = repo; }

    public List<Skill> findAll() { return repo.findAll(); }
    public Optional<Skill> findById(Integer id) { return repo.findById(id); }

    public Skill create(SkillRequest r) {
        Skill s = new Skill();
        s.setSkillName(r.skillName());
        s.setSkillType(r.skillType());
        return repo.save(s);
    }

    public Optional<Skill> update(Integer id, SkillRequest r) {
        return repo.findById(id).map(s -> {
            s.setSkillName(r.skillName());
            s.setSkillType(r.skillType());
            return repo.save(s);
        });
    }

    public boolean delete(Integer id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
