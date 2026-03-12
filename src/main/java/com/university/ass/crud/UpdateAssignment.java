package com.university.ass.crud;

import com.university.ass.model.Assignment;
import com.university.ass.model.AuditLog;
import com.university.ass.model.User;
import com.university.ass.repository.AuditLogRepository;
import com.university.ass.service.AssignmentService;
import com.university.ass.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        if (updatedBy.getRole() == User.Role.ADVISER) {
            // Notify the student
            notificationService.createNotification(
                    saved.getStudent(),
                    "Your assignment (Ref: " + saved.getReferenceNumber() + ") has been updated by your course adviser."
            );
        } else {
            // Notify all advisers
            // This is handled by NotificationObserver via eventPublisher
        }

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
