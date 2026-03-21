package com.university.ass.service;

import com.university.ass.model.Assignment;
import com.university.ass.model.User;
import com.university.ass.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Assignment createAssignment(Assignment assignment) {
        assignment.setReferenceNumber(generateReference());
        logger.info("Creating assignment with reference: {}", assignment.getReferenceNumber());
        Assignment saved = assignmentRepository.save(assignment);
        logger.info("Assignment created successfully with ID: {}", saved.getAssignmentId());
        return saved;
    }

    public List<Assignment> findAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> findAssignmentsByStudent(User student) {
        return assignmentRepository.findByStudent(student);
    }

    public Optional<Assignment> findById(int id) {
        return assignmentRepository.findById(id);
    }

    public Assignment updateAssignment(Assignment assignment) {
        logger.info("Updating assignment ID: {}", assignment.getAssignmentId());
        Assignment saved = assignmentRepository.save(assignment);
        logger.info("Assignment updated successfully. Reference: {}", saved.getReferenceNumber());
        return saved;
    }

    public void deleteAssignment(int id) {
        logger.warn("Deleting assignment with ID: {}", id);
        assignmentRepository.deleteById(id);
        logger.info("Assignment ID: {} deleted successfully", id);
    }

    private String generateReference() {
        return "ASS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}