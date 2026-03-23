package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentWorkRepository extends JpaRepository<StudentWork, Integer> {
    Optional<StudentWork> findByStudentId(Integer studentId);
}
