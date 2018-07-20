package ca.uwindsor.ims.model;

public class ReportlistBo {
private int year;
private String university;
private String country;
private String university_location;


public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public int getYear() {
	return year;
}
public String getUniversity_location() {
	return university_location;
}
public void setUniversity_location(String university_location) {
	this.university_location = university_location;
}
public void setYear(int year) {
	this.year = year;
}
public String getUniversity() {
	return university;
}
public void setUniversity(String university) {
	this.university = university;
}
}
