package ca.uwindsor.ims.dto;

public record StudentInfoRequest(
        Integer year,
        String fname,
        String lname,
        String mname,
        String stuEmail,
        String stuTelephone,
        String gender,
        String canadaStatus,
        String semester,
        String internshipStatus,
        String studentStatus,
        String country
) {}
