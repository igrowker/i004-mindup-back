package com.mindup.core.controllers;

import com.mindup.core.services.EmailVerificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @GetMapping("/api/core/verify")
    public String verifyEmail(@RequestParam String token) {
        boolean isVerified = emailVerificationService.verifyEmail(token);
        if (isVerified) {
            return "Email verified successfully!";
        } else {
            return "Invalid verification token or Expiry date!.";
        }
    }
}
