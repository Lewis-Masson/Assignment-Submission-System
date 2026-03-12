package com.university.ass.repository;

import com.university.ass.model.Assignment;
import com.university.ass.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findByStudent(User student);
    List<Assignment> findByStudentUserId(int studentId);
}