package ca.uwindsor.ims.dto;

public record JobApplicationResponse(
        Integer studentJobId,
        Integer jobId,
        String jobPosition,
        Integer companyId,
        String companyName,
        Integer studentId,
        String studentName,
        String flag
) {}
