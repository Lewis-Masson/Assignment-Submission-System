package com.university.ass.crud;

import com.university.ass.model.Assignment;
import com.university.ass.model.AuditLog;
import com.university.ass.model.User;
import com.university.ass.repository.AuditLogRepository;
import com.university.ass.service.AssignmentService;
import com.university.ass.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.university.ass.service.UserService;
import java.util.List;

@Component
public class CreateAssignment {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public Assignment execute(Assignment assignment, User createdBy) {
        Assignment saved = assignmentService.createAssignment(assignment);

        // Audit log
        AuditLog log = new AuditLog();
        log.setUser(createdBy);
        log.setAction("CREATE");
        log.setEntityType("Assignment");
        log.setEntityId(saved.getAssignmentId());
        auditLogRepository.save(log);

        // Notify the student
        notificationService.createNotification(
                saved.getStudent(),
                "An assignment submission has been created for you by your course adviser. Reference: " + saved.getReferenceNumber()
        );

        // Notify the adviser who created it
        notificationService.createNotification(
                createdBy,
                "Assignment (Ref: " + saved.getReferenceNumber() + ") was created for student "
                + saved.getStudent().getFirstName() + " " + saved.getStudent().getLastName()
        );

        return saved;
    }
}
