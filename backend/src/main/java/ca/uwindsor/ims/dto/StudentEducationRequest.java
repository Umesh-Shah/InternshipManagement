package ca.uwindsor.ims.dto;

public record StudentEducationRequest(
        String degreeType,
        String major,
        String degreeGpa,
        String university,
        String universityLocation
) {}
