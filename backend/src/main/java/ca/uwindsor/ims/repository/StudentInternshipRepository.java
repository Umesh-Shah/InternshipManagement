package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentInternship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentInternshipRepository extends JpaRepository<StudentInternship, Integer> {
    List<StudentInternship> findByStudentId(Integer studentId);
}
