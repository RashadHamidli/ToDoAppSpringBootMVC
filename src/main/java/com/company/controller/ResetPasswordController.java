package com.company.controller;

import com.company.model.dto.request.UserRequest;
import com.company.service.inter.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {
    private final NotificationService notificationService;

    @GetMapping("/reset")
    public String getReset() {
        return "reset-password";
    }
    @GetMapping("/resetpassword")
    public String getResetPassword() {
        return "reset";
    }

    @PostMapping("/reset")
    public String postResetPassword(UserRequest userRequest) {
        String email = userRequest.getEmail();
        notificationService.sendNotificationPassword(email);
        return "reset";
    }
    @PostMapping("/resetpassword")
    public String postResetPassword() {
        return "login";
    }


}
