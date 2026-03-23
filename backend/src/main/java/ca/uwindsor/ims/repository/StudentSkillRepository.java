package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, Integer> {
    List<StudentSkill> findByStudentId(Integer studentId);
    void deleteByStudentId(Integer studentId);
}
