package ca.uwindsor.ims.dto;

public record InternshipStatusRequest(
    Integer studentId,
    Integer companyId,
    Integer jobId,
    Integer internshipId,
    String internshipType,
    String internshipStatus
) {}
