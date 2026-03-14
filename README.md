# Assignment Submission System (ASS)

A web-based university assignment submission system built with Java, Spring Boot and Thymeleaf. Supports two user roles — Student and Course Adviser — with full CRUD operations, notifications and audit logging.

## Features

- Account registration and login with role-based access (Student / Course Adviser)
- Students can submit assignments and receive a generated reference number
- Course Advisers can create, read, edit and delete all assignment submissions
- Two-way Observer Pattern notifications — users are notified when their submissions are modified
- Timestamped notifications and audit log for every action on the system
- Adviser can reset student passwords
- Mark all notifications as read
- Form validation throughout

## Tech Stack

- **Backend**: Java 25, Spring Boot 3.x, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML, CSS
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **IDE**: Apache NetBeans 29

## Project Structure
```
src/main/java/com/university/ass/
├── model/          # Entity classes (User, Assignment, Notification, AuditLog)
├── repository/     # JPA repository interfaces
├── service/        # Business logic
├── controller/     # Web request handlers
├── crud/           # Separate CRUD operation classes
└── observer/       # Observer pattern for notifications
```

## Running Locally

### Prerequisites
- Apache NetBeans 29
- MySQL 8.0
- Java 25

### Setup

1. Clone the repository
2. Create a MySQL database called `ass_db`
3. Update `src/main/resources/application.properties` with your MySQL credentials
4. In NetBeans right click the project → Properties → Actions → Run Project
5. Set the Goal to `spring-boot:run`
6. Run the project with F6

The application will start at `http://localhost:8080`

## Pages

| Page | URL |
|------|-----|
| Landing | `/` |
| Login | `/login` |
| Register | `/register` |
| Student Dashboard | `/student/dashboard` |
| Submit Assignment | `/student/submit` |
| Adviser Dashboard | `/adviser/dashboard` |
| Reset Password | `/adviser/reset-password` |

## Deployment

Deployment to Heroku via GitHub.
