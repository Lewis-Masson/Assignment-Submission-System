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

        // Notify the student who submitted
        notificationService.createNotification(
            saved.getStudent(),
            "Your assignment has been submitted successfully. Reference: " + saved.getReferenceNumber()
        );

        // Notify all advisers
        List<User> advisers = userService.findAllAdvisers();
        for (User adviser : advisers) {
            notificationService.createNotification(
                adviser,
                "Student " + saved.getStudent().getFirstName() + " " + saved.getStudent().getLastName()
                + " submitted a new assignment. Reference: " + saved.getReferenceNumber()
            );
        }

        return saved;
    }
}