package ca.uwindsor.ims.dto;

public record StudentWorkRequest(
        String startDate,
        String endDate,
        String company,
        String companyLocation,
        String position
) {}
