package com.university.ass.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int assignmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false, length = 20)
    private String courseId;

    @Column(nullable = false, length = 20)
    private String session;

    @Column(nullable = false, length = 20)
    private String term;

    @Column(nullable = false)
    private int creditUnits;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @Column(nullable = false, unique = true, length = 20)
    private String referenceNumber;

    @Column(length = 50)
    private String status = "SUBMITTED";

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public int getCreditUnits() { return creditUnits; }
    public void setCreditUnits(int creditUnits) { this.creditUnits = creditUnits; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}