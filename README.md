# Assignment Submission System (ASS)

A web-based university assignment submission system built with Java, Spring Boot and Thymeleaf. Supports two user roles — Student and Course Adviser — with full CRUD operations, notifications, file uploads and audit logging.

---

## Features

**Account & Registration**
- Role-based registration — separate forms for Student and Course Adviser
- Study level selection (Undergraduate, Masters, PhD)
- Funding source selection (SAAS, Student Loans Company, University Scholarships & Bursaries, UKRI for postgraduate)
- Self-funding option — funding dropdown hidden if self-funded
- Form validation throughout — email regex, password minimum 8 characters, mobile number UK format, 8-digit matriculation number (Student), staff ID (Adviser)
- Login with role-based routing to the correct dashboard
- Adviser can reset student passwords

**Assignment Submissions**
- Students can submit assignments and receive a unique generated reference number (ASS-XXXXXXXX)
- Students can edit their own submissions
- Course Advisers can create, read, edit and delete all student submissions
- Every action (create, read, edit, delete) is timestamped and logged to the audit log

**File Uploads**
- Students can attach a Word or PDF document (max 2MB) to any assignment submission
- Students can delete and resubmit a file at any time
- Advisers can view and download all attached documents from the dashboard
- Notifications sent on upload to both the student and all advisers

**Notifications**
- Two-way Observer Pattern notifications throughout
- Notifications generated on: assignment created, edited, deleted and file uploaded
- Students notified when an adviser creates or modifies their submission
- Advisers notified when a student submits, edits or uploads a file
- Mark all notifications as read on both dashboards
- Timestamped notifications displayed on login

---

## Tech Stack

| Layer        | Technology                              |
|--------------|-----------------------------------------|
| Language     | Java 25                                 |
| Framework    | Spring Boot 3.x, Spring Security        |
| Templates    | Thymeleaf                               |
| Database     | MySQL 8.0                               |
| ORM          | Spring Data JPA / Hibernate             |
| Build Tool   | Maven                                   |
| IDE          | Apache NetBeans 29                      |
| Deployment   | Heroku via GitHub                       |

---

## Project Structure

```
src/main/java/com/university/ass/
├── model/          # Entity classes (User, Assignment, Notification, AuditLog)
├── repository/     # JPA repository interfaces
├── service/        # Business logic (UserService, AssignmentService, NotificationService)
├── controller/     # Web request handlers (AuthController, StudentController, AdviserController)
├── crud/           # Separate CRUD operation classes (Create, Read, Update, Delete)
└── observer/       # Observer Pattern (AssignmentObserver, NotificationObserver, AssignmentEventPublisher)
```

---

## Database

Database name: `ass_db`

| Table           | Description                                        |
|-----------------|----------------------------------------------------|
| `users`         | All users with role ENUM (STUDENT / ADVISER)       |
| `assignments`   | Submitted assignments with reference numbers and file data (BLOB) |
| `notifications` | Observer-generated notifications per user          |
| `audit_log`     | Timestamped log of all system actions              |

---

## Running Locally

### Prerequisites
- Apache NetBeans 29
- MySQL 8.0
- Java 25

---

## Pages

| Page                        | URL                        | Role    |
|-----------------------------|----------------------------|---------|
| Landing                     | `/`                        | Public  |
| Login                       | `/login`                   | Public  |
| Register (role selector)    | `/register`                | Public  |
| Register — Student          | `/register/student`        | Public  |
| Register — Course Adviser   | `/register/adviser`        | Public  |
| Student Dashboard           | `/student/dashboard`       | Student |
| Submit Assignment           | `/student/submit`          | Student |
| Edit Assignment             | `/student/edit/{id}`       | Student |
| Adviser Dashboard           | `/adviser/dashboard`       | Adviser |
| Create Assignment           | `/adviser/create`          | Adviser |
| Edit Assignment             | `/adviser/edit/{id}`       | Adviser |
| Reset Student Password      | `/adviser/reset-password`  | Adviser |

---

## Deployment

Deployment to Heroku via GitHub.
