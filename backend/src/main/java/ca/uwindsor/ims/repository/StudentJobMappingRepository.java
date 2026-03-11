package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentJobMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentJobMappingRepository extends JpaRepository<StudentJobMapping, Integer> {
    List<StudentJobMapping> findByStudentId(Integer studentId);
    List<StudentJobMapping> findByJobId(Integer jobId);
    List<StudentJobMapping> findByFlag(String flag);
    java.util.Optional<StudentJobMapping> findByStudentIdAndJobId(Integer studentId, Integer jobId);
}
