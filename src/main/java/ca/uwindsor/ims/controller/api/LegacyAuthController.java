package ca.uwindsor.ims.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.uwindsor.ims.model.LoginBo;
import ca.uwindsor.ims.service.RegLoginService;

@RestController
@RequestMapping("/api/legacy/auth")
public class LegacyAuthController {
    
    @Autowired
    private RegLoginService regLoginService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        LoginBo user = regLoginService.validateLogin(username, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginBo loginBo) {
        boolean success = regLoginService.registerUser(loginBo);
        if (success) {
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.badRequest().body("Failed to register user");
    }
    
    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam String username, @RequestParam String newPassword) {
        boolean success = regLoginService.updatePassword(username, newPassword);
        if (success) {
            return ResponseEntity.ok("Password updated successfully");
        }
        return ResponseEntity.badRequest().body("Failed to update password");
    }
} 