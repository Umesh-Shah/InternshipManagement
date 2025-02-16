package ca.uwindsor.ims.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "login")
public class LoginBo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOGIN_ID")
    private Integer loginId;

    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PWD")
    private String password;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "FLAG")
    private String flag;

    // Default constructor for JPA
    public LoginBo() {}

    // Constructor for creating new users
    public LoginBo(Integer studentId, String username, String password, String userType, String flag) {
        this.studentId = studentId;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.flag = flag;
    }

    // Constructor with all fields
    public LoginBo(Integer loginId, Integer studentId, String username, String password, String userType, String flag) {
        this.loginId = loginId;
        this.studentId = studentId;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.flag = flag;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
} 