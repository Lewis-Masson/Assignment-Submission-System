package com.university.ass.crud;

import com.university.ass.model.Assignment;
import com.university.ass.model.AuditLog;
import com.university.ass.model.User;
import com.university.ass.repository.AuditLogRepository;
import com.university.ass.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class ReadAssignment {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public List<Assignment> getAllAssignments(User requestedBy) {
        logAction(requestedBy, -1);
        return assignmentService.findAllAssignments();
    }

    public List<Assignment> getStudentAssignments(User student, User requestedBy) {
    logAction(requestedBy, student.getUserId());
    return assignmentService.findAssignmentsByStudent(student);
}

    public Optional<Assignment> getById(int id, User requestedBy) {
        logAction(requestedBy, id);
        return assignmentService.findById(id);
    }

    private void logAction(User user, int entityId) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction("READ");
        log.setEntityType("Assignment");
        log.setEntityId(entityId);
        auditLogRepository.save(log);
    }
}