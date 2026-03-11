package ca.uwindsor.ims.dto;

public record InternshipStatusResponse(
    Integer studentInternshipId,
    Integer studentId,
    String studentName,
    Integer companyId,
    String companyName,
    Integer jobId,
    String jobPosition,
    Integer internshipId,
    String internshipType,
    String internshipStatus,
    String studentStatus
) {}
