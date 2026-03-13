package com.university.ass.crud;

import com.university.ass.model.AuditLog;
import com.university.ass.model.User;
import com.university.ass.repository.AuditLogRepository;
import com.university.ass.service.AssignmentService;
import com.university.ass.service.NotificationService;
import com.university.ass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DeleteAssignment {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    public void execute(int assignmentId, User deletedBy) {

        // Fetch assignment before deletion
        assignmentService.findById(assignmentId).ifPresent(assignment -> {

            // Notify the student
            notificationService.createNotification(
                    assignment.getStudent(),
                    "Your assignment (Ref: " + assignment.getReferenceNumber()
                    + ") was deleted by your course adviser."
            );

            // Notify all advisers
            List<User> advisers = userService.findAllAdvisers();
            for (User adviser : advisers) {
                notificationService.createNotification(
                        adviser,
                        "You deleted assignment (Ref: " + assignment.getReferenceNumber()
                        + ") belonging to student " + assignment.getStudent().getFirstName()
                        + " " + assignment.getStudent().getLastName() + "."
                );
            }

            // Audit log before deletion
            AuditLog log = new AuditLog();
            log.setUser(deletedBy);
            log.setAction("DELETE");
            log.setEntityType("Assignment");
            log.setEntityId(assignmentId);
            auditLogRepository.save(log);

            // Now delete
            assignmentService.deleteAssignment(assignmentId);
        });
    }
}
