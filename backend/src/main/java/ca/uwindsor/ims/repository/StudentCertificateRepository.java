package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentCertificateRepository extends JpaRepository<StudentCertificate, Integer> {
    Optional<StudentCertificate> findByStudentId(String studentId);
}
