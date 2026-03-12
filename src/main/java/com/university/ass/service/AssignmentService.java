package com.university.ass.service;

import com.university.ass.model.Assignment;
import com.university.ass.model.User;
import com.university.ass.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Assignment createAssignment(Assignment assignment) {
        assignment.setReferenceNumber(generateReference());
        return assignmentRepository.save(assignment);
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
        return assignmentRepository.save(assignment);
    }

    public void deleteAssignment(int id) {
        assignmentRepository.deleteById(id);
    }

    private String generateReference() {
        return "ASS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}