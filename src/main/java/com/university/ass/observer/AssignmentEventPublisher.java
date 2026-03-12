package com.university.ass.observer;

import com.university.ass.model.Assignment;
import com.university.ass.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AssignmentEventPublisher {

    @Autowired
    private List<AssignmentObserver> observers;

    public void notifyObservers(Assignment assignment, User updatedBy) {
        for (AssignmentObserver observer : observers) {
            observer.onAssignmentUpdated(assignment, updatedBy);
        }
    }
}