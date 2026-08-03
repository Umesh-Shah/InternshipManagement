package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.StudentJobMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentJobMappingRepository extends JpaRepository<StudentJobMapping, Integer> {
    List<StudentJobMapping> findByStudentId(Integer studentId);
    List<StudentJobMapping> findByJobId(Integer jobId);
    List<StudentJobMapping> findByFlag(String flag);
    java.util.Optional<StudentJobMapping> findByStudentIdAndJobId(Integer studentId, Integer jobId);

    /**
     * Single-query fetch: joins student_job_master → job → company → student.
     * Returns a flat projection used by JobApplicationService to avoid N+1.
     */
    @NativeQuery("""
            SELECT
                m.STUDENT_JOB_ID  AS studentJobId,
                m.JOB_ID          AS jobId,
                j.JOB_POSITION    AS jobPosition,
                j.company_id      AS companyId,
                c.COMPANY_NAME    AS companyName,
                m.STUDENT_ID      AS studentId,
                CONCAT(s.FNAME, ' ', s.LNAME) AS studentName,
                m.flag            AS flag
            FROM student_job_master m
            LEFT JOIN job j ON j.JOB_ID     = m.JOB_ID
            LEFT JOIN company c ON c.COMPANY_ID = j.company_id
            LEFT JOIN student s ON s.STUDENT_ID = m.STUDENT_ID
            WHERE (:flag IS NULL OR m.flag = :flag)
              AND (:studentId IS NULL OR m.STUDENT_ID = :studentId)
              AND (:jobId IS NULL OR m.JOB_ID = :jobId)
            """)
    List<JobApplicationProjection> findApplications(
            @Param("flag") String flag,
            @Param("studentId") Integer studentId,
            @Param("jobId") Integer jobId);

    interface JobApplicationProjection {
        Integer getStudentJobId();
        Integer getJobId();
        String  getJobPosition();
        Integer getCompanyId();
        String  getCompanyName();
        Integer getStudentId();
        String  getStudentName();
        String  getFlag();
    }
}
