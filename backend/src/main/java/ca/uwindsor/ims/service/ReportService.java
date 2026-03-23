package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final EntityManager em;

    public ReportService(EntityManager em) {
        this.em = em;
    }

    public List<StudentReportRow> studentsReport(Integer year, String country,
                                                  String semester, String internshipStatus,
                                                  String studentStatus) {
        StringBuilder jpql = new StringBuilder(
            "SELECT new ca.uwindsor.ims.dto.StudentReportRow(" +
            "s.studentId, s.fname, s.lname, s.year, s.country, s.semester, s.internshipStatus, s.studentStatus" +
            ") FROM StudentInfo s WHERE 1=1");
        Map<String, Object> params = new LinkedHashMap<>();

        if (year != null) {
            jpql.append(" AND s.year = :year");
            params.put("year", year);
        }
        if (country != null && !country.isBlank()) {
            jpql.append(" AND s.country = :country");
            params.put("country", country);
        }
        if (semester != null && !semester.isBlank()) {
            jpql.append(" AND s.semester = :semester");
            params.put("semester", semester);
        }
        if (internshipStatus != null && !internshipStatus.isBlank()) {
            jpql.append(" AND s.internshipStatus = :internshipStatus");
            params.put("internshipStatus", internshipStatus);
        }
        if (studentStatus != null && !studentStatus.isBlank()) {
            jpql.append(" AND s.studentStatus = :studentStatus");
            params.put("studentStatus", studentStatus);
        }
        jpql.append(" ORDER BY s.lname, s.fname");

        TypedQuery<StudentReportRow> q = em.createQuery(jpql.toString(), StudentReportRow.class);
        params.forEach(q::setParameter);
        return q.getResultList();
    }

    public List<CompanyReportRow> companiesReport(String city) {
        StringBuilder jpql = new StringBuilder(
            "SELECT new ca.uwindsor.ims.dto.CompanyReportRow(" +
            "c.companyId, c.companyName, c.city, c.country, c.email, c.telephone" +
            ") FROM Company c WHERE 1=1");
        Map<String, Object> params = new LinkedHashMap<>();

        if (city != null && !city.isBlank()) {
            jpql.append(" AND c.city = :city");
            params.put("city", city);
        }
        jpql.append(" ORDER BY c.companyName");

        TypedQuery<CompanyReportRow> q = em.createQuery(jpql.toString(), CompanyReportRow.class);
        params.forEach(q::setParameter);
        return q.getResultList();
    }

    public List<InternshipTypeReportRow> internshipTypesReport(String internshipType) {
        StringBuilder jpql = new StringBuilder(
            "SELECT new ca.uwindsor.ims.dto.InternshipTypeReportRow(" +
            "si.internshipType, COUNT(si)" +
            ") FROM StudentInternship si WHERE si.internshipType IS NOT NULL");
        Map<String, Object> params = new LinkedHashMap<>();

        if (internshipType != null && !internshipType.isBlank()) {
            jpql.append(" AND si.internshipType = :internshipType");
            params.put("internshipType", internshipType);
        }
        jpql.append(" GROUP BY si.internshipType ORDER BY si.internshipType");

        TypedQuery<InternshipTypeReportRow> q = em.createQuery(jpql.toString(), InternshipTypeReportRow.class);
        params.forEach(q::setParameter);
        return q.getResultList();
    }

    public List<GpaReportRow> gpaReport(Integer year, String country,
                                         String university, String universityLocation,
                                         String degreeType) {
        StringBuilder jpql = new StringBuilder(
            "SELECT new ca.uwindsor.ims.dto.GpaReportRow(" +
            "s.studentId, s.fname, s.lname, e.university, e.universityLocation, e.degreeType, e.degreeGpa" +
            ") FROM StudentInfo s, StudentEducation e WHERE s.studentId = e.studentId");
        Map<String, Object> params = new LinkedHashMap<>();

        if (year != null) {
            jpql.append(" AND s.year = :year");
            params.put("year", year);
        }
        if (country != null && !country.isBlank()) {
            jpql.append(" AND s.country = :country");
            params.put("country", country);
        }
        if (university != null && !university.isBlank()) {
            jpql.append(" AND e.university = :university");
            params.put("university", university);
        }
        if (universityLocation != null && !universityLocation.isBlank()) {
            jpql.append(" AND e.universityLocation = :universityLocation");
            params.put("universityLocation", universityLocation);
        }
        if (degreeType != null && !degreeType.isBlank()) {
            jpql.append(" AND e.degreeType = :degreeType");
            params.put("degreeType", degreeType);
        }
        jpql.append(" ORDER BY s.lname, s.fname");

        TypedQuery<GpaReportRow> q = em.createQuery(jpql.toString(), GpaReportRow.class);
        params.forEach(q::setParameter);
        return q.getResultList();
    }

    public List<JobReportRow> jobsReport(Integer companyId) {
        StringBuilder jpql = new StringBuilder(
            "SELECT new ca.uwindsor.ims.dto.JobReportRow(" +
            "j.jobId, j.jobPosition, c.companyName, " +
            "(SELECT COUNT(m) FROM StudentJobMapping m WHERE m.jobId = j.jobId)" +
            ") FROM Job j, Company c WHERE j.companyId = c.companyId");
        Map<String, Object> params = new LinkedHashMap<>();

        if (companyId != null) {
            jpql.append(" AND j.companyId = :companyId");
            params.put("companyId", companyId);
        }
        jpql.append(" ORDER BY c.companyName, j.jobPosition");

        TypedQuery<JobReportRow> q = em.createQuery(jpql.toString(), JobReportRow.class);
        params.forEach(q::setParameter);
        return q.getResultList();
    }

    public ReportFiltersResponse getFilters() {
        List<Integer> years = em.createQuery(
            "SELECT DISTINCT s.year FROM StudentInfo s WHERE s.year IS NOT NULL ORDER BY s.year", Integer.class)
            .getResultList();

        List<String> countries = em.createQuery(
            "SELECT DISTINCT s.country FROM StudentInfo s WHERE s.country IS NOT NULL ORDER BY s.country", String.class)
            .getResultList();

        List<String> universities = em.createQuery(
            "SELECT DISTINCT e.university FROM StudentEducation e WHERE e.university IS NOT NULL ORDER BY e.university", String.class)
            .getResultList();

        List<String> universityLocations = em.createQuery(
            "SELECT DISTINCT e.universityLocation FROM StudentEducation e WHERE e.universityLocation IS NOT NULL ORDER BY e.universityLocation", String.class)
            .getResultList();

        List<String> internshipTypes = em.createQuery(
            "SELECT DISTINCT si.internshipType FROM StudentInternship si WHERE si.internshipType IS NOT NULL ORDER BY si.internshipType", String.class)
            .getResultList();

        return new ReportFiltersResponse(years, countries, universities, universityLocations, internshipTypes);
    }
}
