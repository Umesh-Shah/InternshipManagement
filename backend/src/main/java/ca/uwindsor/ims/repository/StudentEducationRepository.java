package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentEducationRepository extends JpaRepository<StudentEducation, Integer> {
    Optional<StudentEducation> findByStudentId(Integer studentId);
}
