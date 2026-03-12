package com.university.ass.observer;

import com.university.ass.model.Assignment;
import com.university.ass.model.User;

public interface AssignmentObserver {
    void onAssignmentUpdated(Assignment assignment, User updatedBy);
}