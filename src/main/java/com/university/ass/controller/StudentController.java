package com.university.ass.controller;

import com.university.ass.crud.CreateAssignment;
import com.university.ass.crud.ReadAssignment;
import com.university.ass.crud.UpdateAssignment;
import com.university.ass.model.Assignment;
import com.university.ass.model.User;
import com.university.ass.observer.AssignmentEventPublisher;
import com.university.ass.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private CreateAssignment createAssignment;

    @Autowired
    private ReadAssignment readAssignment;

    @Autowired
    private UpdateAssignment updateAssignment;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AssignmentEventPublisher eventPublisher;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("assignments", readAssignment.getStudentAssignments(user, user));
        model.addAttribute("notifications", notificationService.getUnreadNotifications(user));
        return "student/dashboard";
    }

    @GetMapping("/submit")
    public String submitPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("assignment", new Assignment());
        return "student/submit";
    }

    @PostMapping("/submit")
    public String submitAssignment(@ModelAttribute Assignment assignment,
                                   HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        assignment.setStudent(user);
        createAssignment.execute(assignment, user);
        return "redirect:/student/dashboard?submitted";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        readAssignment.getById(id, user).ifPresent(a -> model.addAttribute("assignment", a));
        return "student/edit";
    }

    @PostMapping("/edit/{id}")
    public String editAssignment(@PathVariable int id,
                                  @ModelAttribute Assignment assignment,
                                  HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        assignment.setAssignmentId(id);
        assignment.setStudent(user);
        Assignment updated = updateAssignment.execute(assignment, user);
        eventPublisher.notifyObservers(updated, user);
        return "redirect:/student/dashboard?updated";
    }
}