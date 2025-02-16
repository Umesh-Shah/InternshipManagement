package ca.uwindsor.ims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "login")
public record LoginBo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOGIN_ID")
    int loginId,
    
    @Column(name = "STUDENT_ID")
    int studentId,
    
    @Column(name = "USERNAME")
    String username,
    
    @Column(name = "PWD")
    String password,
    
    @Column(name = "USER_TYPE")
    String userType,
    
    @Column(name = "FLAG")
    String flag
) {}
