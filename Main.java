import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        FeedbackSystem<Integer, String> sharedFeedbackSystem = new FeedbackSystem<>();
        
       
        Admin admin = new Admin("admin@example.com", "adminpass");
        Professor professor = new Professor("prof@example.com", "profpass", "Dr. Smith", sharedFeedbackSystem, admin);
        Student student = new Student("student@example.com", "studentpass", 1, 1, 0, 0, 0, sharedFeedbackSystem, null, null);
        TeachingAssistant ta = new TeachingAssistant("ta@example.com", "tapass", 2, 1, 0, 0, 0, sharedFeedbackSystem, professor);

        Course defaultCourse1 = new Course("CS101", "Introduction to Computer Science", "John Doe", 3, "None", "MWF 10:00-11:00", 1, 1);
        Course defaultCourse2 = new Course("CS202", "Data Structures", "Jane Smith", 4, "CS101", "TTH 2:00-3:30", 2, 1);
        Course defaultCourse3 = new Course("CS203", "Operating Systems", "Bob Johnson", 3, "CS101", "TTH 4:00-5:30", 3, 1);
        Course defaultCourse4 = new Course("CS204", "Artificial Intelligence", "Alice Lee", 3, "CS101", "TTH 6:00-7:30", 4, 1);

        admin.addCourse(defaultCourse1);
        admin.addCourse(defaultCourse2);
        admin.addCourse(defaultCourse3);
        admin.addCourse(defaultCourse4);
        professor.addCourse(defaultCourse1);
        professor.addCourse(defaultCourse2);
        professor.addCourse(defaultCourse3);
        professor.addCourse(defaultCourse4);

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Admin Login");
            System.out.println("2. Professor Login");
            System.out.println("3. Student Login");
            System.out.println("4. TA Login");
            System.out.println("5. Exit");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    try {
                        if (loginUser(admin, sc)) {
                            adminMenu(admin, sc);
                        }
                        throw new InvalidLoginException("Invalid email or password");
                    } catch (InvalidLoginException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        if (loginUser(professor, sc)) {
                            professorMenu(professor, sc);
                        }
                        throw new InvalidLoginException("Invalid email or password");
                    } catch (InvalidLoginException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Enter student ID:");
                    int studentId = sc.nextInt();
                    sc.nextLine();

                    Student studentFound = findStudentById(admin, studentId);
                    if (studentFound != null) {
                        try {
                            if (loginUser(studentFound, sc)) {
                                studentMenu(studentFound, admin, professor, sc);
                            }
                            throw new InvalidLoginException("Invalid email or password");
                        } catch (InvalidLoginException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid student ID.");
                    }
                    break;
                case 4:
                    try {
                        if (loginUser(ta, sc)) {
                            taMenu(ta, professor, sc);
                        }
                        throw new InvalidLoginException("Invalid email or password");
                    } catch (InvalidLoginException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting the system...");
                    sc.close();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static boolean loginUser(User user, Scanner sc) {
        System.out.println("Enter email:");
        String email = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();

        return user.getEmail().equals(email) && user.getPassword().equals(password);
    }

    private static Student findStudentById(Admin admin, int studentId) {
        for (Student s : admin.getStudents()) {
            if (s.getId() == studentId) {
                return s;
            }
        }
        return null;
    }
    private static void taMenu(TeachingAssistant ta, Professor professor, Scanner sc) {
        boolean taRunning = true;

        while (taRunning) {
            System.out.println("\nTA Menu:");
            System.out.println("1. View Student Grades");
            System.out.println("2. View Completed Courses");
            System.out.println("3. Assist Professor");
            System.out.println("4. Exit");

            int option = sc.nextInt();
            sc.nextLine(); 

            switch (option) {
                case 1:
                    System.out.println("Enter student ID to view grades:");
                    int studentId = sc.nextInt();
                    sc.nextLine();
                    ta.viewGrades(professor, studentId);
                    break;
                case 2:
                    System.out.println("Enter student ID:");
                    int studentId4 = sc.nextInt();
                    sc.nextLine();
                    ta.viewCompletedCourses(professor, studentId4);
                    break;
                case 3:
                    ta.assistProfessor(professor);
                    break;
                case 4:
                    taRunning = false;
                    System.out.println("Exiting TA Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void manageStudents(Admin admin, Scanner sc) {
        boolean studentRunning = true;

        while (studentRunning) {
            System.out.println("\nManage Students:");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. View Students");
            System.out.println("4. Back to Admin Menu");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addStudent(admin, sc);
                    break;
                case 2:
                    removeStudent(admin, sc);
                    break;
                case 3:
                    viewStudents(admin);
                    break;
                case 4:
                    studentRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addStudent(Admin admin, Scanner sc) {
        System.out.println("Enter student email:");
        String email = sc.nextLine();
        System.out.println("Enter student password:");
        String password = sc.nextLine();
        System.out.println("Enter student ID:");
        int id = sc.nextInt();
        sc.nextLine(); 
        System.out.println("Enter current semester:");
        int semester = sc.nextInt();
        System.out.println("Enter total credits completed:");
        int credits = sc.nextInt();
        System.out.println("Enter total courses completed:");
        int coursesCompleted = sc.nextInt();
        System.out.println("Enter GPA:");
        int gpa = sc.nextInt();
        sc.nextLine(); 
        FeedbackSystem<Integer, String> sharedFeedbackSystem = new FeedbackSystem<>();
        Student student = new Student(email, password, id, semester, credits, coursesCompleted, gpa,
                sharedFeedbackSystem, null, null);
        admin.addStudent(student);
    }

    private static void removeStudent(Admin admin, Scanner sc) {
        System.out.println("Enter student ID to remove:");
        int studentId = sc.nextInt();
        sc.nextLine(); 
        admin.removeStudent(studentId);
    }

    private static void viewStudents(Admin admin) {
        List<Student> students = admin.getStudents();
        if (students.isEmpty()) {
            System.out.println("No students available.");
        } else {
            for (Student student : students) {
                System.out.println("Student ID: " + student.getId() + ", Email: " + student.getEmail());
            }
        }
    }

    private static void adminMenu(Admin admin, Scanner sc) {
        boolean adminRunning = true;

        while (adminRunning) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Manage Course Catalog");
            System.out.println("2. Manage Professors");
            System.out.println("3. Manage Students"); 
            System.out.println("4. View Complaints");
            System.out.println("5. Resolve Complaints");
            System.out.println("6. Exit");

            int option = sc.nextInt();
            sc.nextLine(); 
            switch (option) {
                case 1:
                    manageCourseCatalog(admin, sc);
                    break;
                case 2:
                    manageProfessors(admin, sc);
                    break;
                case 3:
                    manageStudents(admin, sc); 
                    break;
                case 4:
                    viewComplaints(admin.getComplaintSystem(), sc);
                    break;
                case 5:
                    resolveComplaints(admin.getComplaintSystem(), sc);
                    break;
                case 6:
                    adminRunning = false;
                    System.out.println("Exiting Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void manageCourseCatalog(Admin admin, Scanner sc) {
        boolean catalogRunning = true;

        while (catalogRunning) {
            System.out.println("\nManage Course Catalog:");
            System.out.println("1. Add Course");
            System.out.println("2. Remove Course");
            System.out.println("3. View Courses");
            System.out.println("4. Back to Admin Menu");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    addCourse(admin, sc);
                    break;
                case 2:
                    removeCourse(admin, sc);
                    break;
                case 3:
                    viewCourses(admin, sc);
                    break;
                case 4:
                    catalogRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addCourse(Admin admin, Scanner sc) {
        System.out.println("Enter course code:");
        String code = sc.nextLine();

        
        if (admin.isCoursePresent(code)) {
            System.out.println("Course with code " + code + " already exists.");
            return;
        }

        System.out.println("Enter title:");
        String title = sc.nextLine();
        System.out.println("Enter professor:");
        String professor = sc.nextLine();
        System.out.println("Enter credits:");
        int credits = sc.nextInt();
        sc.nextLine(); 
        System.out.println("Enter prerequisites:");
        String prerequisites = sc.nextLine();
        System.out.println("Enter schedule:");
        String schedule = sc.nextLine();
        System.out.println("Enter semester:");
        int semester = sc.nextInt();
        sc.nextLine(); 

        Course course = new Course(code, title, professor, credits, prerequisites, schedule, semester, 0);
        admin.addCourse(course);
    }

    private static void removeCourse(Admin admin, Scanner sc) {
        System.out.println("Enter course code to remove:");
        String code = sc.nextLine();
        admin.removeCourse(code);
    }

    private static void viewCourses(Admin admin, Scanner sc) {
        List<Course> courses = admin.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            for (Course course : courses) {
                course.viewCourse();
                System.out.println();
            }
        }
    }
    private static void manageProfessors(Admin admin, Scanner sc) {
        FeedbackSystem<Integer, String> sharedFeedbackSystem = new FeedbackSystem<>();
        boolean professorRunning = true;

        while (professorRunning) {
            System.out.println("\nManage Professors:");
            System.out.println("1. Add Professor");
            System.out.println("2. Remove Professor");
            System.out.println("3. Set Professor Name");
            System.out.println("4. View Professors");
            System.out.println("5. Assign Course to Professor");
            System.out.println("6. Back to Admin Menu");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    addProfessor(admin, sharedFeedbackSystem, sc);
                    break;
                case 2:
                    removeProfessor(admin, sc);
                    break;
                case 3:
                    setProfessorName(admin, sc);
                    break;
                case 4:
                    viewProfessors(admin, sc);
                    break;
                case 5:
                    System.out.println("Enter professor name:");
                    String professorName = sc.nextLine();
                    Professor professor = getProfessor(admin, professorName);
                    if (professor != null) {
                        assignCourseToProfessor(admin, professor, sc);
                    } else {
                        System.out.println("Professor not found.");
                    }
                    break;
                case 6:
                    professorRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static Professor getProfessor(Admin admin, String professorName) {
        for (Professor professor : admin.getProfessors()) {
            if (professor.getName().equals(professorName)) {
                return professor;
            }
        }
        return null;
    }

    private static void assignCourseToProfessor(Admin admin, Professor professor, Scanner sc) {
        System.out.println("Enter course code:");
        String courseCode = sc.nextLine();

        for (Course course : admin.getCourses()) {
            if (course.getCourseCode().equals(courseCode)) {
                professor.addCourse(course);
                System.out.println("Course " + courseCode + " assigned to Professor " + professor.getName());
                return;
            }
        }
        System.out.println("Course not found.");
    }
    private static void addProfessor(Admin admin, FeedbackSystem<Integer, String> sharedFeedbackSystem, Scanner sc) {
        System.out.println("Enter professor email:");
        String email = sc.nextLine();
        System.out.println("Enter professor password:");
        String password = sc.nextLine();
        System.out.println("Enter professor name:");
        String name = sc.nextLine();

        Professor professor = new Professor(email, password, name, sharedFeedbackSystem, null);
        admin.addProfessor(professor);
    }

    private static void removeProfessor(Admin admin, Scanner sc) {
        System.out.println("Enter professor name to remove:");
        String name = sc.nextLine();
        admin.removeProfessor(name);
    }

    private static void setProfessorName(Admin admin, Scanner sc) {
        System.out.println("Enter current professor name:");
        String oldName = sc.nextLine();
        System.out.println("Enter new professor name:");
        String newName = sc.nextLine();
        admin.setProfessor(oldName, newName);
    }

    private static void viewProfessors(Admin admin, Scanner sc) {
        List<Professor> professors = admin.getProfessors();
        if (professors.isEmpty()) {
            System.out.println("No professors available.");
        } else {
            for (Professor professor : professors) {
                System.out.println("Name: " + professor.getName() + ", Email: " + professor.getEmail());
            }
        }
    }

    private static void viewComplaints(ComplaintSystem complaintSystem, Scanner sc) {
        complaintSystem.viewComplaints();
    }

    private static void resolveComplaints(ComplaintSystem complaintSystem, Scanner sc) {
        System.out.println("Enter complaint ID to resolve:");
        int id = sc.nextInt();
        sc.nextLine();
        complaintSystem.resolveComplaint(id);
    }

    private static void professorMenu(Professor professor, Scanner sc) {
        boolean professorRunning = true;
        while (professorRunning) {
            System.out.println("\nProfessor Menu:");
            System.out.println("1. View Assigned Courses");
            System.out.println("2. Track Student Progress");
            System.out.println("3. Mark Course Completed");
            System.out.println("4. Set Student Grade");
            System.out.println("5. View Student Grades");
            System.out.println("6. View Completed Courses");
            System.out.println("7. View Feedback");
            System.out.println("8. Exit");

            int option = sc.nextInt();
            sc.nextLine(); 

            switch (option) {
                case 1:
                    professor.viewAssignedCourses();
                    break;
                case 2:
                    System.out.println("Enter student ID:");
                    int studentId = sc.nextInt();
                    sc.nextLine(); 
                    professor.trackProgress(studentId);
                    break;
                case 3:
                    markCourseCompleted(professor, sc);
                    break;
                case 4:
                    setStudentGrade(professor, sc);
                    break;
                case 5:
                    viewStudentGrades(professor, sc);
                    break;
                case 6:
                    viewCompletedCourses(professor, sc);
                    break;
                case 7:
                    professor.viewFeedback();
                    break;
                case 8:
                    professorRunning = false;
                    System.out.println("Exiting Professor Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void markCourseCompleted(Professor professor, Scanner sc) {
        System.out.println("Enter course code to mark as completed:");
        String courseCode = sc.nextLine();
        System.out.println("Enter student ID:");
        int studentId = sc.nextInt();
        sc.nextLine();
        professor.markCourseCompleted(studentId, courseCode);
    }

    private static void setStudentGrade(Professor professor, Scanner sc) {
        System.out.println("Enter student ID:");
        int studentId = sc.nextInt();
        sc.nextLine(); 
        System.out.println("Enter course code:");
        String courseCode = sc.nextLine();
        System.out.println("Enter grade:");
        String grade = sc.nextLine();
        sc.nextLine(); 
        professor.setGrade(studentId, courseCode, grade);
    }

    private static void viewStudentGrades(Professor professor, Scanner sc) {
        System.out.println("Enter student ID:");
        int studentId = sc.nextInt();
        sc.nextLine(); 
        professor.viewGrades(studentId);
    }

    private static void viewCompletedCourses(Professor professor, Scanner sc) {
        System.out.println("Enter student ID:");
        int studentId = sc.nextInt();
        sc.nextLine();
        professor.viewCompletedCourses(studentId);
    }

    private static void viewFeedBack(Professor professor, Scanner sc) {
        professor.viewFeedback();
    }

    private static void studentMenu(Student student, Admin admin, Professor professor, Scanner sc) {
        boolean studentRunning = true;

        while (studentRunning) {
            System.out.println("\nStudent Menu:");
            System.out.println("1. View Available Courses");
            System.out.println("2. Register Course");
            System.out.println("3. Drop Course");
            System.out.println("4. View Grades");
            System.out.println("5. View Schedule");
            System.out.println("6. Track Progress");
            System.out.println("7. View Registered Courses");
            System.out.println("8. Submit Complaint");
            System.out.println("9. Submit Feedback");
            System.out.println("10. Exit");

            int option = sc.nextInt();
            sc.nextLine(); 

            switch (option) {
                case 1:
                    viewAvailableCourses(student, admin, sc);
                    break;
                case 2:
                    registerCourse(student, sc, admin.getCourses());
                    break;
                case 3:
                    dropCourse(student, sc);
                    break;
                case 4:
                    student.viewGrades(professor);
                    break;
                case 5:
                    student.viewSchedule();
                    break;
                case 6:
                    student.trackProgress(professor);
                    break;
                case 7:
                    student.viewRegisteredCourses();
                    break;
                case 8:
                    submitComplaint(student, admin.getComplaintSystem(), sc);
                    break;
                case 9:
                    student.submitFeedback();
                    break;
                case 10:
                    studentRunning = false;
                    System.out.println("Exiting Student Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void submitfeedback(Student student) {
        student.submitFeedback();
        System.out.println("Feedback submitted");
    }

    private static void viewAvailableCourses(Student student, Admin admin, Scanner sc) {
        List<Course> courses = admin.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            System.out.println("Available Courses:");
            for (Course course : courses) {
                course.viewCourse();
                System.out.println();
            }
        }
    }

    private static void registerCourse(Student student, Scanner sc, List<Course> availableCourses) {
        System.out.println("Enter course code to register:");
        String code = sc.nextLine();

        Course courseToRegister = null;
        for (Course course : availableCourses) {
            if (course.getCourseCode().equals(code)) {
                courseToRegister = course;
                break;
            }
        }

        if (courseToRegister != null) {
            student.registerCourse(courseToRegister);
            
        } else {
            System.out.println("Course not found or already registered.");
        }
    }

    private static void dropCourse(Student student, Scanner sc) {
        System.out.println("Enter course code to drop:");
        String code = sc.nextLine();
        
        
        LocalDate dropDeadline = LocalDate.of(2024, 9, 30);
        
        
        student.dropCourse(code,dropDeadline );
        
       
    }

    private static void submitComplaint(Student student, ComplaintSystem complaintSystem, Scanner sc) {
        System.out.println("Enter student ID:");
        int id = sc.nextInt();
        sc.nextLine(); 
        System.out.println("Enter complaint description:");
        String description = sc.nextLine();
        Complaint complaint = new Complaint(description, id);
        complaintSystem.addComplaint(complaint);
        System.out.println("Complaint submitted with ID: " + complaint.getId());
    }
}