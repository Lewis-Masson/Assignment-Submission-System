package com.university.ass.crud;

import com.university.ass.model.AuditLog;
import com.university.ass.model.User;
import com.university.ass.repository.AuditLogRepository;
import com.university.ass.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteAssignment {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void execute(int assignmentId, User deletedBy) {
        // Audit log before deletion
        AuditLog log = new AuditLog();
        log.setUser(deletedBy);
        log.setAction("DELETE");
        log.setEntityType("Assignment");
        log.setEntityId(assignmentId);
        auditLogRepository.save(log);

        assignmentService.deleteAssignment(assignmentId);
    }
}