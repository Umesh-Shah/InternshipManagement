package ca.uwindsor.ims.dto;

public class CompanyReportRow {
    private Integer companyId;
    private String companyName;
    private String city;
    private String country;
    private String email;
    private String telephone;

    public CompanyReportRow() {}

    public CompanyReportRow(Integer companyId, String companyName, String city,
                            String country, String email, String telephone) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.city = city;
        this.country = country;
        this.email = email;
        this.telephone = telephone;
    }

    public Integer getCompanyId() { return companyId; }
    public void setCompanyId(Integer v) { companyId = v; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String v) { companyName = v; }
    public String getCity() { return city; }
    public void setCity(String v) { city = v; }
    public String getCountry() { return country; }
    public void setCountry(String v) { country = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String v) { telephone = v; }
}
