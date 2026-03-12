package com.university.ass.controller;

import com.university.ass.crud.*;
import com.university.ass.model.Assignment;
import com.university.ass.model.User;
import com.university.ass.observer.AssignmentEventPublisher;
import com.university.ass.service.NotificationService;
import com.university.ass.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.university.ass.service.AssignmentService;

@Controller
@RequestMapping("/adviser")
public class AdviserController {

    @Autowired
    private CreateAssignment createAssignment;

    @Autowired
    private ReadAssignment readAssignment;

    @Autowired
    private UpdateAssignment updateAssignment;

    @Autowired
    private DeleteAssignment deleteAssignment;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private AssignmentEventPublisher eventPublisher;
    
    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("assignments", readAssignment.getAllAssignments(user));
        model.addAttribute("notifications", notificationService.getUnreadNotifications(user));
        model.addAttribute("students", userService.findAllStudents());
        return "adviser/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        readAssignment.getById(id, user).ifPresent(a -> model.addAttribute("assignment", a));
        return "adviser/edit";
    }

@PostMapping("/edit/{id}")
public String editAssignment(@PathVariable int id,
                              @ModelAttribute Assignment assignment,
                              HttpSession session) {
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) return "redirect:/login";

    assignmentService.findById(id).ifPresent(existing -> {
        existing.setCourseId(assignment.getCourseId());
        existing.setCreditUnits(assignment.getCreditUnits());
        existing.setSession(assignment.getSession());
        existing.setTerm(assignment.getTerm());
        existing.setAdditionalInfo(assignment.getAdditionalInfo());
        existing.setStatus(assignment.getStatus());
        Assignment updated = updateAssignment.execute(existing, user);
        eventPublisher.notifyObservers(updated, user);
    });

    return "redirect:/adviser/dashboard?updated";
}

    @GetMapping("/delete/{id}")
    public String deleteAssignment(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        deleteAssignment.execute(id, user);
        return "redirect:/adviser/dashboard?deleted";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("students", userService.findAllStudents());
        return "adviser/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam int studentId,
                                 @RequestParam String newPassword,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        userService.resetPassword(studentId, newPassword);
        return "redirect:/adviser/dashboard?passwordReset";
    }
}