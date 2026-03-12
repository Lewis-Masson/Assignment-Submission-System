package com.university.ass.crud;

import com.university.ass.model.Assignment;
import com.university.ass.model.AuditLog;
import com.university.ass.model.User;
import com.university.ass.repository.AuditLogRepository;
import com.university.ass.service.AssignmentService;
import com.university.ass.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UpdateAssignment {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public Assignment execute(Assignment updatedAssignment, User updatedBy) {
        Assignment saved = assignmentService.updateAssignment(updatedAssignment);

        // Notify the other party
        User recipient;
        String message;

        if (updatedBy.getRole() == User.Role.ADVISER) {
            recipient = saved.getStudent();
            message = "Your assignment submission (Ref: " + saved.getReferenceNumber() + ") has been updated by your course adviser.";
        } else {
            // Need to notify all advisers - handled in controller
            recipient = updatedBy;
            message = "Student " + updatedBy.getFirstName() + " " + updatedBy.getLastName() + " updated assignment (Ref: " + saved.getReferenceNumber() + ").";
        }

        notificationService.createNotification(recipient, message);

        // Audit log
        AuditLog log = new AuditLog();
        log.setUser(updatedBy);
        log.setAction("UPDATE");
        log.setEntityType("Assignment");
        log.setEntityId(saved.getAssignmentId());
        auditLogRepository.save(log);

        return saved;
    }
}