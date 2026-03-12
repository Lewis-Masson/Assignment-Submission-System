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
import com.university.ass.service.AssignmentService;

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

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("assignments", readAssignment.getStudentAssignments(user, user));
        model.addAttribute("notifications", notificationService.getUnreadNotifications(user));
        return "student/dashboard";
    }

    @GetMapping("/submit")
    public String submitPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("assignment", new Assignment());
        return "student/submit";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        readAssignment.getById(id, user).ifPresent(a -> model.addAttribute("assignment", a));
        return "student/edit";
    }

    @GetMapping("/notifications/clear")
    public String clearNotifications(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        notificationService.markAllAsRead(user);
        return "redirect:/student/dashboard";
    }

    @PostMapping("/submit")
    public String submitAssignment(@ModelAttribute Assignment assignment,
            HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        assignment.setStudent(user);
        createAssignment.execute(assignment, user);
        return "redirect:/student/dashboard?submitted";
    }

    @PostMapping("/edit/{id}")
    public String editAssignment(@PathVariable int id,
            @ModelAttribute Assignment assignment,
            HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        assignmentService.findById(id).ifPresent(existing -> {
            existing.setCourseId(assignment.getCourseId());
            existing.setCreditUnits(assignment.getCreditUnits());
            existing.setSession(assignment.getSession());
            existing.setTerm(assignment.getTerm());
            existing.setAdditionalInfo(assignment.getAdditionalInfo());
            Assignment updated = updateAssignment.execute(existing, user);
            eventPublisher.notifyObservers(updated, user);
        });

        return "redirect:/student/dashboard?updated";
    }

}
