package com.university.ass.controller;

import com.university.ass.model.User;
import com.university.ass.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.university.ass.service.NotificationService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        return userService.findByEmail(email).map(user -> {
            if (org.springframework.security.crypto.bcrypt.BCrypt.checkpw(password, user.getPassword())) {
                session.setAttribute("loggedInUser", user);
                if (user.getRole() == User.Role.ADVISER) {
                    return "redirect:/adviser/dashboard";
                } else {
                    return "redirect:/student/dashboard";
                }
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "login";
            }
        }).orElseGet(() -> {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        });
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/notifications/clear")
    public String clearNotifications(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        notificationService.markAllAsRead(user);
        return "redirect:/adviser/dashboard";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered");
            return "register";
        }
        userService.registerUser(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
