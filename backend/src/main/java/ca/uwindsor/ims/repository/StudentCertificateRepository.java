package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentCertificateRepository extends JpaRepository<StudentCertificate, Integer> {
    // student_certificate.STUDENT_ID is stored as VARCHAR in the DB (legacy schema).
    Optional<StudentCertificate> findByStudentId(String studentId);

    default Optional<StudentCertificate> findByStudentId(Integer studentId) {
        return findByStudentId(String.valueOf(studentId));
    }
}
