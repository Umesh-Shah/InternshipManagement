package ca.uwindsor.ims.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "VBCT_LOGIN")
public record VbctLoginBO(
	@Id
	@Column(name = "LOGIN_ID")
	String loginId,
	
	@Column(name = "LOGIN_NAME")
	String loginName,
	
	@Column(name = "LOGIN_PASSWORD")
	String loginPassword,
	
	@Column(name = "FIRST_NAME")
	String firstName,
	
	@Column(name = "MIDDLE_NAME")
	String middleName,
	
	@Column(name = "LAST_NAME")
	String lastName,
	
	@Column(name = "DSGN_ID")
	String designationId,
	
	@Column(name = "EMAIL_ID")
	String emailId,
	
	@Column(name = "DOB")
	String dateOfBirth,
	
	@Column(name = "PHONE_NO")
	String phoneNo,
	
	@Column(name = "SERVICE_FLG")
	String serviceFlag,
	
	@Column(name = "LANG_ID")
	String languageId,
	
	@Column(name = "CRT_DT")
	LocalDateTime createdDate,
	
	@Column(name = "CRT_USR")
	String createdUser,
	
	@Column(name = "LST_UPD_USR")
	String lastUpdatedUser,
	
	@Column(name = "LST_UPD_DT")
	LocalDateTime lastUpdatedDate,
	
	@Column(name = "SERVICE_EXPIRY_DT")
	LocalDateTime serviceExpiryDate,
	
	@Column(name = "FORGOT_PWD")
	String forgotPassword,
	
	@Column(name = "FRST_LOGIN_FLG")
	String firstLoginFlag,
	
	@Column(name = "PSWD_EXPIRE_DT")
	LocalDateTime passwordExpiryDate,
	
	@Column(name = "LST_PSWD_UPD_DT")
	LocalDateTime lastPasswordUpdateDate,
	
	@Column(name = "LOGIN_STARTTIME")
	LocalDateTime loginStartTime,
	
	@Column(name = "LOGIN_ENDTIME")
	LocalDateTime loginEndTime,
	
	@Column(name = "IPADDR")
	String ipAddress
) {}
