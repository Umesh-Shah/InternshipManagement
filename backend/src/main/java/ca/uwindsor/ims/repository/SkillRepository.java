package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}
