package ca.uwindsor.ims.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.uwindsor.ims.model.VbctLoginBO;
import ca.uwindsor.ims.service.RegLoginService;
import ca.uwindsor.ims.dto.LoginRequest;
import ca.uwindsor.ims.dto.PasswordChangeRequest;
import ca.uwindsor.ims.dto.ApiResponse;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private RegLoginService regLoginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<VbctLoginBO>> login(@RequestBody LoginRequest request) {
        Optional<VbctLoginBO> user = regLoginService.getEmployeeList(request.getUsername(), request.getPassword());
        
        return user.map(u -> ResponseEntity.ok(new ApiResponse<>(true, "Login successful", u)))
                  .orElse(ResponseEntity.ok(new ApiResponse<>(false, "Invalid credentials", null)));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody PasswordChangeRequest request) {
        boolean isValid = regLoginService.checkPassword(request.getLoginId(), request.getOldPassword());
        if (!isValid) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Invalid current password", null));
        }

        boolean changed = regLoginService.changePassword(request.getLoginId(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse<>(changed, 
            changed ? "Password changed successfully" : "Failed to change password", null));
    }

    @PostMapping("/check-login")
    public ResponseEntity<ApiResponse<Boolean>> checkLogin(@RequestBody LoginRequest request) {
        boolean valid = regLoginService.checkLogin(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new ApiResponse<>(valid, 
            valid ? "Valid credentials" : "Invalid credentials", valid));
    }
} 