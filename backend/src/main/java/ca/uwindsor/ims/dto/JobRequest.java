package ca.uwindsor.ims.dto;

public record JobRequest(
    String jobPosition, String description, String requirements,
    Integer salary, Integer companyId, String responsibilities,
    String jobSkill, String internshipType
) {}
