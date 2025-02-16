package ca.uwindsor.ims.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import ca.uwindsor.ims.config.TestConfig;
import ca.uwindsor.ims.dao.RegLoginDao;
import ca.uwindsor.ims.exception.DatabaseException;
import ca.uwindsor.ims.model.VbctLoginBO;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@Transactional
class RegLoginDaoImplTest {
    
    @Autowired
    private RegLoginDao regLoginDao;
    
    private VbctLoginBO testEmployee;
    
    @BeforeEach
    void setUp() {
        testEmployee = new VbctLoginBO();
        testEmployee.setLoginId("EMP001");
        testEmployee.setLoginName("testuser");
        testEmployee.setLoginPassword("password123");
        testEmployee.setFirstName("John");
        testEmployee.setMiddleName("M");
        testEmployee.setLastName("Doe");
        testEmployee.setDesignationId("MANAGER");
        testEmployee.setEmailId("john.doe@example.com");
        testEmployee.setDateOfBirth("1990-01-01");
        testEmployee.setPhoneNo("1234567890");
        testEmployee.setServiceFlag("A");
        testEmployee.setLanguageId("EN");
        testEmployee.setCreatedDate(LocalDateTime.now());
        testEmployee.setCreatedUser("SYSTEM");
        testEmployee.setLastUpdatedUser("SYSTEM");
        testEmployee.setLastUpdatedDate(LocalDateTime.now());
        testEmployee.setServiceExpiryDate(LocalDateTime.now().plusYears(1));
        testEmployee.setForgotPassword("N");
        testEmployee.setFirstLoginFlag("N");
        testEmployee.setPasswordExpiryDate(LocalDateTime.now().plusMonths(3));
        testEmployee.setLastPasswordUpdateDate(LocalDateTime.now());
        testEmployee.setLoginStartTime(LocalDateTime.now());
        testEmployee.setLoginEndTime(LocalDateTime.now());
        testEmployee.setIpAddress("127.0.0.1");
    }
    
    @Test
    void changePassword_ShouldUpdatePasswordSuccessfully() {
        // Given
        var savedEmployee = regLoginDao.saveDataComon(testEmployee);
        var newPassword = "newPass123";
        
        // When
        var result = regLoginDao.changePassword(savedEmployee.getLoginName(), newPassword);
        
        // Then
        assertThat(result).isTrue();
        var updatedEmployee = regLoginDao.getEmployeeList(savedEmployee.getLoginName(), newPassword);
        assertThat(updatedEmployee).isPresent();
        assertThat(updatedEmployee.get().getLoginPassword()).isEqualTo(newPassword);
    }
    
    @Test
    void checkPassword_ShouldReturnTrueForValidCredentials() {
        // Given
        var savedEmployee = regLoginDao.saveDataComon(testEmployee);
        
        // When
        var result = regLoginDao.checkPassword(savedEmployee.getLoginName(), savedEmployee.getLoginPassword());
        
        // Then
        assertThat(result).isTrue();
    }
    
    @Test
    void checkPassword_ShouldReturnFalseForInvalidCredentials() {
        // Given
        var savedEmployee = regLoginDao.saveDataComon(testEmployee);
        
        // When
        var result = regLoginDao.checkPassword(savedEmployee.getLoginName(), "wrongpassword");
        
        // Then
        assertThat(result).isFalse();
    }
    
    @Test
    void getEmployeeList_ShouldReturnEmployeeForValidCredentials() {
        // Given
        var savedEmployee = regLoginDao.saveDataComon(testEmployee);
        
        // When
        var result = regLoginDao.getEmployeeList(savedEmployee.getLoginName(), savedEmployee.getLoginPassword());
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getLoginId()).isEqualTo(savedEmployee.getLoginId());
    }
    
    @Test
    void getEmployeeList_ShouldReturnEmptyForInvalidCredentials() {
        // Given
        var savedEmployee = regLoginDao.saveDataComon(testEmployee);
        
        // When
        var result = regLoginDao.getEmployeeList(savedEmployee.getLoginName(), "wrongpassword");
        
        // Then
        assertThat(result).isEmpty();
    }
    
    @Test
    void saveDataComon_ShouldPersistEntity() {
        // When
        var savedEmployee = regLoginDao.saveDataComon(testEmployee);
        
        // Then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getLoginId()).isEqualTo(testEmployee.getLoginId());
    }
    
    @Test
    void checkLogin_ShouldReturnTrueForValidCredentials() {
        // Given
        var savedEmployee = regLoginDao.saveDataComon(testEmployee);
        
        // When
        var result = regLoginDao.checkLogin(savedEmployee.getLoginName(), savedEmployee.getLoginPassword());
        
        // Then
        assertThat(result).isTrue();
    }
    
    @Test
    void checkLogin_ShouldReturnFalseForInvalidCredentials() {
        // Given
        var savedEmployee = regLoginDao.saveDataComon(testEmployee);
        
        // When
        var result = regLoginDao.checkLogin(savedEmployee.getLoginName(), "wrongpassword");
        
        // Then
        assertThat(result).isFalse();
    }
    
    @Test
    void saveDataComon_ShouldThrowDatabaseExceptionOnError() {
        // Given
        VbctLoginBO invalidEmployee = new VbctLoginBO();
        // Invalid: null ID
        invalidEmployee.setLoginName("testuser");
        invalidEmployee.setLoginPassword("password123");
        invalidEmployee.setFirstName("John");
        invalidEmployee.setMiddleName("M");
        invalidEmployee.setLastName("Doe");
        invalidEmployee.setDesignationId("MANAGER");
        invalidEmployee.setEmailId("john.doe@example.com");
        invalidEmployee.setDateOfBirth("1990-01-01");
        invalidEmployee.setPhoneNo("1234567890");
        invalidEmployee.setServiceFlag("A");
        invalidEmployee.setLanguageId("EN");
        invalidEmployee.setCreatedDate(LocalDateTime.now());
        invalidEmployee.setCreatedUser("SYSTEM");
        invalidEmployee.setLastUpdatedUser("SYSTEM");
        invalidEmployee.setLastUpdatedDate(LocalDateTime.now());
        invalidEmployee.setServiceExpiryDate(LocalDateTime.now().plusYears(1));
        invalidEmployee.setForgotPassword("N");
        invalidEmployee.setFirstLoginFlag("N");
        invalidEmployee.setPasswordExpiryDate(LocalDateTime.now().plusMonths(3));
        invalidEmployee.setLastPasswordUpdateDate(LocalDateTime.now());
        invalidEmployee.setLoginStartTime(LocalDateTime.now());
        invalidEmployee.setLoginEndTime(LocalDateTime.now());
        invalidEmployee.setIpAddress("127.0.0.1");
        
        // Then
        assertThatThrownBy(() -> regLoginDao.saveDataComon(invalidEmployee))
            .isInstanceOf(DatabaseException.class);
    }
} 