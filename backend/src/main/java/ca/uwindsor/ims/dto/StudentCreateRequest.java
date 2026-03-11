package ca.uwindsor.ims.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentCreateRequest(
        @NotNull Integer studentId,
        Integer year,
        @NotBlank String fname,
        @NotBlank String lname,
        String mname,
        @NotBlank @Email String stuEmail,
        String stuTelephone,
        String gender,
        String canadaStatus,
        String semester,
        String country
) {}
