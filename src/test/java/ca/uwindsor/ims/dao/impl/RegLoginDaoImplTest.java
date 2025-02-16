package ca.uwindsor.ims.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import ca.uwindsor.ims.config.TestConfig;
import ca.uwindsor.ims.dao.RegLoginDao;
import ca.uwindsor.ims.model.LoginBo;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@Transactional
class RegLoginDaoImplTest {
    
    @Autowired
    private RegLoginDao regLoginDao;
    
    private LoginBo testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new LoginBo(
            null,
            1,
            "testuser",
            "password123",
            "STUDENT",
            "A"
        );
    }
    
    @Test
    void registerUser_ShouldSaveUserSuccessfully() {
        boolean result = regLoginDao.registerUser(testUser);
        assertThat(result).isTrue();
        
        // Verify user was saved
        LoginBo savedUser = regLoginDao.validateLogin("testuser", "password123");
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
    }
    
    @Test
    void validateLogin_WithValidCredentials_ShouldReturnUser() {
        // First register the user
        regLoginDao.registerUser(testUser);
        
        // Then try to validate login
        LoginBo result = regLoginDao.validateLogin("testuser", "password123");
        
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getUserType()).isEqualTo("STUDENT");
    }
    
    @Test
    void validateLogin_WithInvalidCredentials_ShouldReturnNull() {
        LoginBo result = regLoginDao.validateLogin("nonexistent", "wrongpass");
        assertThat(result).isNull();
    }
    
    @Test
    void updatePassword_WithValidUser_ShouldSucceed() {
        // First register the user
        regLoginDao.registerUser(testUser);
        
        // Then update password
        boolean result = regLoginDao.updatePassword("testuser", "newpassword123");
        
        assertThat(result).isTrue();
        
        // Verify new password works
        LoginBo updatedUser = regLoginDao.validateLogin("testuser", "newpassword123");
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getPassword()).isEqualTo("newpassword123");
    }
    
    @Test
    void updatePassword_WithNonexistentUser_ShouldReturnFalse() {
        boolean result = regLoginDao.updatePassword("nonexistent", "newpass");
        assertThat(result).isFalse();
    }
} 