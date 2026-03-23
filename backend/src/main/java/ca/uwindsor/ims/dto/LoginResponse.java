package ca.uwindsor.ims.dto;

public record LoginResponse(String role, Integer studentId, String username) {}
