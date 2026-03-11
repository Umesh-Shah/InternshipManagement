package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentInfoRepository extends JpaRepository<StudentInfo, Integer> {
    Optional<StudentInfo> findByStudentId(Integer studentId);
}
