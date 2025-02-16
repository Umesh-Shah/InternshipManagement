package ca.uwindsor.ims.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uwindsor.ims.dto.LoginRequest;
import ca.uwindsor.ims.dto.PasswordChangeRequest;
import ca.uwindsor.ims.model.VbctLoginBO;
import ca.uwindsor.ims.service.RegLoginService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private RegLoginService regLoginService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void login_Success() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "password123");
        VbctLoginBO user = new VbctLoginBO();
        user.setLoginId("test001");
        user.setLoginName("testuser");
        
        when(regLoginService.getEmployeeList(any(), any())).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.loginId").value("test001"));
    }

    @Test
    void login_Failure() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");
        when(regLoginService.getEmployeeList(any(), any())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid credentials"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void changePassword_Success() throws Exception {
        // Arrange
        PasswordChangeRequest request = new PasswordChangeRequest("test001", "oldpass", "newpass");
        when(regLoginService.checkPassword(any(), any())).thenReturn(true);
        when(regLoginService.changePassword(any(), any())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password changed successfully"));
    }

    @Test
    void changePassword_InvalidOldPassword() throws Exception {
        // Arrange
        PasswordChangeRequest request = new PasswordChangeRequest("test001", "wrongpass", "newpass");
        when(regLoginService.checkPassword(any(), any())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid current password"));
    }

    @Test
    void checkLogin_Success() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "password123");
        when(regLoginService.checkLogin(any(), any())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/auth/check-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Valid credentials"))
                .andExpect(jsonPath("$.data").value(true));
    }
} 