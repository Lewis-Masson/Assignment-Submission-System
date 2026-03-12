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
public class CreateAssignment {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private NotificationService notificationService;

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

        return saved;
    }
}