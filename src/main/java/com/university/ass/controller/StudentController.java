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
import com.university.ass.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

    @Autowired
    private UserService userService;

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

    //File storage method
    @PostMapping("/upload/{id}")
    public String uploadFile(@PathVariable int id,
            @RequestParam("file") MultipartFile file,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null
                || (!contentType.equals("application/pdf")
                && !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                && !contentType.equals("application/msword"))) {
            return "redirect:/student/dashboard?fileerror";
        }

        // Validate file size (1MB)
        if (file.getSize() > 1 * 1024 * 1024) {
            return "redirect:/student/dashboard?filesizerror";
        }

        assignmentService.findById(id).ifPresent(existing -> {
            try {
                existing.setFileData(file.getBytes());
                existing.setFileName(file.getOriginalFilename());
                existing.setFileType(contentType);
                assignmentService.updateAssignment(existing);

                // Notify the student
                notificationService.createNotification(
                        user,
                        "Your file \"" + file.getOriginalFilename() + "\" has been attached to assignment (Ref: " + existing.getReferenceNumber() + ")."
                );

                // Notify all advisers
                List<User> advisers = userService.findAllAdvisers();
                for (User adviser : advisers) {
                    notificationService.createNotification(
                            adviser,
                            "Student " + user.getFirstName() + " " + user.getLastName()
                            + " attached a document to assignment (Ref: " + existing.getReferenceNumber() + ")."
                    );
                }

            } catch (IOException e) {
            }
        });
        return "redirect:/student/dashboard?fileuploaded";
    }

    @GetMapping("/delete-file/{id}")
    public String deleteFile(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        assignmentService.findById(id).ifPresent(existing -> {
            existing.setFileData(null);
            existing.setFileName(null);
            existing.setFileType(null);
            assignmentService.updateAssignment(existing);
        });

        return "redirect:/student/dashboard?filedeleted";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return assignmentService.findById(id).map(a -> {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + a.getFileName() + "\"")
                    .contentType(MediaType.parseMediaType(a.getFileType()))
                    .body(a.getFileData());
        }).orElse(ResponseEntity.notFound().build());
    }

}
