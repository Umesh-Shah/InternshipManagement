package ca.uwindsor.ims.dto;

public record LoginResponse(
        String token,
        String role,
        Integer studentId,
        String username
) {}
