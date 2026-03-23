package ca.uwindsor.ims.dto;

import jakarta.validation.constraints.NotNull;

public record JobApplicationRequest(
        @NotNull Integer studentId,
        @NotNull Integer jobId
) {}
