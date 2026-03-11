package ca.uwindsor.ims.dto;

public record CompanyRequest(
    String companyName, String address, String city, String postalCode,
    String country, String contactPersonFname, String contactPersonLname,
    String contactPersonPosition, String telephone, String email,
    String companyWebsite, String notes
) {}
