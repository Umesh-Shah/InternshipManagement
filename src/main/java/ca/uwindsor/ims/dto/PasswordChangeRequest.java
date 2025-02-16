package ca.uwindsor.ims.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordChangeRequest {
    
    @NotBlank(message = "Login ID is required")
    private String loginId;
    
    @NotBlank(message = "Old password is required")
    private String oldPassword;
    
    @NotBlank(message = "New password is required")
    private String newPassword;

    // Default constructor
    public PasswordChangeRequest() {}

    // Constructor with fields
    public PasswordChangeRequest(String loginId, String oldPassword, String newPassword) {
        this.loginId = loginId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // Getters and setters
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
} 