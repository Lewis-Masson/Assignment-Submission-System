package com.university.ass.observer;

import com.university.ass.model.Assignment;
import com.university.ass.model.User;
import com.university.ass.service.NotificationService;
import com.university.ass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class NotificationObserver implements AssignmentObserver {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Override
    public void onAssignmentUpdated(Assignment assignment, User updatedBy) {
        if (updatedBy.getRole() == User.Role.ADVISER) {
            notificationService.createNotification(
                assignment.getStudent(),
                "Your assignment (Ref: " + assignment.getReferenceNumber() + ") has been updated by your course adviser."
            );
        } else {
            List<User> advisers = userService.findAllAdvisers();
            for (User adviser : advisers) {
                notificationService.createNotification(
                    adviser,
                    "Student " + updatedBy.getFirstName() + " " + updatedBy.getLastName() +
                    " updated assignment (Ref: " + assignment.getReferenceNumber() + ")."
                );
            }
        }
    }
}