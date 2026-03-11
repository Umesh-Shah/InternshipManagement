package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CITY")
    private String city;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "CONTACT_PEARSON_FNAME")
    private String contactPersonFname;

    @Column(name = "CONTACT_PERSON_LNAME")
    private String contactPersonLname;

    @Column(name = "CONTACT_PEARSON_POSITION")
    private String contactPersonPosition;

    @Column(name = "TELEPHONE")
    private String telephone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "COMPANY_WEBSITE")
    private String companyWebsite;

    @Column(name = "NOTES")
    private String notes;

    public Integer getCompanyId() { return companyId; }
    public void setCompanyId(Integer v) { companyId = v; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String v) { companyName = v; }
    public String getAddress() { return address; }
    public void setAddress(String v) { address = v; }
    public String getCity() { return city; }
    public void setCity(String v) { city = v; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String v) { postalCode = v; }
    public String getCountry() { return country; }
    public void setCountry(String v) { country = v; }
    public String getContactPersonFname() { return contactPersonFname; }
    public void setContactPersonFname(String v) { contactPersonFname = v; }
    public String getContactPersonLname() { return contactPersonLname; }
    public void setContactPersonLname(String v) { contactPersonLname = v; }
    public String getContactPersonPosition() { return contactPersonPosition; }
    public void setContactPersonPosition(String v) { contactPersonPosition = v; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String v) { telephone = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
    public String getCompanyWebsite() { return companyWebsite; }
    public void setCompanyWebsite(String v) { companyWebsite = v; }
    public String getNotes() { return notes; }
    public void setNotes(String v) { notes = v; }
}
