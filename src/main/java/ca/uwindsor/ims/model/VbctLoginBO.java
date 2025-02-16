package ca.uwindsor.ims.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "VBCT_LOGIN")
public class VbctLoginBO {
	
	@Id
	@Column(name = "LOGIN_ID")
	private String loginId;
	
	@Column(name = "LOGIN_NAME")
	private String loginName;
	
	@Column(name = "LOGIN_PASSWORD")
	private String loginPassword;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "MIDDLE_NAME")
	private String middleName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "DSGN_ID")
	private String designationId;
	
	@Column(name = "EMAIL_ID")
	private String emailId;
	
	@Column(name = "DOB")
	private String dateOfBirth;
	
	@Column(name = "PHONE_NO")
	private String phoneNo;
	
	@Column(name = "SERVICE_FLG")
	private String serviceFlag;
	
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Column(name = "CRT_DT")
	private LocalDateTime createdDate;
	
	@Column(name = "CRT_USR")
	private String createdUser;
	
	@Column(name = "LST_UPD_USR")
	private String lastUpdatedUser;
	
	@Column(name = "LST_UPD_DT")
	private LocalDateTime lastUpdatedDate;
	
	@Column(name = "SERVICE_EXPIRY_DT")
	private LocalDateTime serviceExpiryDate;
	
	@Column(name = "FORGOT_PWD")
	private String forgotPassword;
	
	@Column(name = "FRST_LOGIN_FLG")
	private String firstLoginFlag;
	
	@Column(name = "PSWD_EXPIRE_DT")
	private LocalDateTime passwordExpiryDate;
	
	@Column(name = "LST_PSWD_UPD_DT")
	private LocalDateTime lastPasswordUpdateDate;
	
	@Column(name = "LOGIN_STARTTIME")
	private LocalDateTime loginStartTime;
	
	@Column(name = "LOGIN_ENDTIME")
	private LocalDateTime loginEndTime;
	
	@Column(name = "IPADDR")
	private String ipAddress;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDesignationId() {
		return designationId;
	}

	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getServiceFlag() {
		return serviceFlag;
	}

	public void setServiceFlag(String serviceFlag) {
		this.serviceFlag = serviceFlag;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getLastUpdatedUser() {
		return lastUpdatedUser;
	}

	public void setLastUpdatedUser(String lastUpdatedUser) {
		this.lastUpdatedUser = lastUpdatedUser;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public LocalDateTime getServiceExpiryDate() {
		return serviceExpiryDate;
	}

	public void setServiceExpiryDate(LocalDateTime serviceExpiryDate) {
		this.serviceExpiryDate = serviceExpiryDate;
	}

	public String getForgotPassword() {
		return forgotPassword;
	}

	public void setForgotPassword(String forgotPassword) {
		this.forgotPassword = forgotPassword;
	}

	public String getFirstLoginFlag() {
		return firstLoginFlag;
	}

	public void setFirstLoginFlag(String firstLoginFlag) {
		this.firstLoginFlag = firstLoginFlag;
	}

	public LocalDateTime getPasswordExpiryDate() {
		return passwordExpiryDate;
	}

	public void setPasswordExpiryDate(LocalDateTime passwordExpiryDate) {
		this.passwordExpiryDate = passwordExpiryDate;
	}

	public LocalDateTime getLastPasswordUpdateDate() {
		return lastPasswordUpdateDate;
	}

	public void setLastPasswordUpdateDate(LocalDateTime lastPasswordUpdateDate) {
		this.lastPasswordUpdateDate = lastPasswordUpdateDate;
	}

	public LocalDateTime getLoginStartTime() {
		return loginStartTime;
	}

	public void setLoginStartTime(LocalDateTime loginStartTime) {
		this.loginStartTime = loginStartTime;
	}

	public LocalDateTime getLoginEndTime() {
		return loginEndTime;
	}

	public void setLoginEndTime(LocalDateTime loginEndTime) {
		this.loginEndTime = loginEndTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
