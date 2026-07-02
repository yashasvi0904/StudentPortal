import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;

public class Student extends User {
    private int id;
    private int studentId;
    private int currSemester;
    private int cgpa;
    private double gpa;
    private int sgpa;
    private ArrayList<Course> registeredCourses;
    private ArrayList<Course> completedCourses;
    private List<Student> students;
    private int totalCredits;

    
    private FeedbackSystem feedbackSystem;
    private Professor professor;
    private static final Scanner sc = new Scanner(System.in);
    private Admin admin;

   
    public Student(String email, String password, int Id, int currSemester, int cgpa, double gpa, int sgpa,
            FeedbackSystem feedbackSystem, Professor professor, Admin admin) {

        super(email, password);
        this.id = Id;
        this.studentId = Id;
        this.currSemester = currSemester;
        this.cgpa = cgpa;
        this.gpa = gpa;
        this.sgpa = sgpa;
        this.registeredCourses = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
        this.totalCredits = 0;
        this.feedbackSystem = feedbackSystem;
        this.students = new ArrayList<>();
        this.professor = professor;
        this.admin = admin;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

   
    public Student(int studentId, String email, int currSemester) {
        super(email, ""); 
        this.studentId = studentId;
        this.currSemester = currSemester;
        this.cgpa = 0;
        this.gpa = 0;
        this.sgpa = 0;
        this.registeredCourses = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
        this.totalCredits = 0;
    }

    public static Student findStudentByIdentifier(List<Student> students, String identifier) {
        for (Student student : students) {
           
            if (String.valueOf(student.getId()).equals(identifier)) {
                return student; 
            }
        }
        return null; 
    }

    public void viewRegisteredCourses() {

        for (Course course : registeredCourses) {
            System.out.println(course.getTitle() + " - " + course.getCourseCode());

        }

    }

    public void viewGrades(Professor professor) {
        // Use the logged-in student's own ID instead of prompting for it again.
        professor.viewGrades(this.getId());
    }

    public void submitFeedback() {

        int rating = -1;
        

        while (true) {
            System.out.println("Enter rating (1 to 5):");
            rating = sc.nextInt();
            if (rating > 0 && rating <= 5) {
                break;
            } else {
                System.out.println("Rating should be between 1 and 5. Please try again.");
            }
        }
        sc.nextLine();
        System.out.println("Enter feedback:");
        String feedbackContent = sc.nextLine();
        Feedback<Integer, String> feedback = new Feedback<>(rating, feedbackContent);
        feedbackSystem.addFeedback(feedback);

        System.out.println("Feedback submitted successfully.");
    }

    public void viewAvailableCourses(List<Course> availableCourses) {
        if (availableCourses.isEmpty()) {
            System.out.println("No available courses to display for student ID: " + studentId);
        } else {
            System.out.println("Available courses for student ID: " + studentId);
            for (Course course : availableCourses) {
                course.viewCourse();
                System.out.println();
            }
        }
    }

 
    public void registerCourse(Course course) {
        try {
            int inputId = this.getId();

            // Prevent registering for the same course twice.
            for (Course registered : registeredCourses) {
                if (registered.getCourseCode().equals(course.getCourseCode())) {
                    System.out.println("You are already registered for " + course.getTitle() + ".");
                    return;
                }
            }

            // A course unlocks only once its prerequisite has been completed.
            if (!prerequisiteMet(course)) {
                throw new IllegalArgumentException("You must complete the prerequisite ("
                        + course.getPrerequisites() + ") before registering for " + course.getTitle() + ".");
            }

            if (course.enrollStudent(inputId)) {
                registeredCourses.add(course);
                totalCredits += course.getCredits();
                System.out.println("Registered for " + course.getTitle() + " for student ID: " + inputId);
            } else {
                System.out.println("Could not enroll.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    public boolean prerequisiteMet(Course course) {
        String pre = course.getPrerequisites();
        if (pre == null || pre.trim().isEmpty() || pre.equalsIgnoreCase("None")) {
            return true;
        }
        // Support one or more comma/semicolon-separated prerequisite codes.
        for (String req : pre.split("[,;]+")) {
            String code = req.trim();
            if (code.isEmpty()) continue;
            boolean have = false;
            for (Course done : completedCourses) {
                if (done.getCourseCode().equalsIgnoreCase(code)) {
                    have = true;
                    break;
                }
            }
            if (!have) return false;
        }
        return true;
    }

    public void viewGrades() {
        int inputId = this.getId();

        // Grades are recorded by the professor, so route through them rather than
        // fabricating an "A" for every completed course.
        if (professor != null) {
            professor.viewGrades(inputId);
        } else if (completedCourses.isEmpty()) {
            System.out.println("No courses completed yet for student ID: " + inputId);
        } else {
            System.out.println("Completed courses (grades pending from professor) for student ID: " + inputId + ":");
            for (Course course : completedCourses) {
                System.out.println(course.getTitle());
            }
        }
    }

    public void viewSchedule() {
        int inputId = this.getId();

        if (registeredCourses.isEmpty()) {
            System.out.println("No courses registered for the current semester for student ID: " + inputId);
            return;
        }

        System.out.println("Weekly Schedule for Semester " + currSemester + " (Student ID: " + inputId + "):");
        for (Course course : registeredCourses) {
            System.out.println(
                    course.getTitle() + " - " + course.getSchedule() + " (Professor: " + course.getProfessor() + ")");
        }
    }

    public void trackProgress(Professor professor) {
        professor.trackProgress(this.getId());
    }
    public void dropCourse(String courseCode, LocalDate dropDeadline) {
        try {
            int inputId = this.getId();

            LocalDate currentDate = LocalDate.now();
            if (currentDate.isAfter(dropDeadline)) {
                throw new DropDeadlinePassedException("The drop deadline has passed. You cannot drop the course.");
            }
    
          
            boolean found = false;
            for (Course course : registeredCourses) {
                if (course.getCourseCode().equals(courseCode)) {
                   
                    // course.dropStudent(this.studentId);
                    registeredCourses.remove(course);
                    totalCredits -= course.getCredits();
                    System.out.println("Dropped course: " + course.getTitle() + " for student ID: " + inputId);
                    found = true;
                    break;
                }
            }
    
            if (!found) {
                System.out.println(
                        "Course with code " + courseCode + " not found in registered courses for student ID: " + inputId);
            }
    
        } catch (DropDeadlinePassedException e) {
            System.out.println(e.getMessage());  
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid student ID.");
            sc.nextLine(); 
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());  
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    public void submitComplaint(String description) {
        System.out.println("Enter student ID:");
        int inputId = sc.nextInt();
        sc.nextLine(); 

        for (Student student : students) {
            if (student.getId() == inputId) {
                this.studentId = inputId;
                break;
            }
        }
        Complaint complaint = new Complaint(description, studentId);
        ComplaintSystem complaintSystem = new ComplaintSystem();
        complaintSystem.addComplaint(complaint);
        System.out.println("Complaint submitted with ID: " + complaint.getId() + " for student ID: " + inputId);
    }

    
    public ArrayList<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    public int getId() {
        return studentId; 
    }

    public void addStudent(Student student) {
        students.add(student);
    }
    
    public void setRegisteredCourses(ArrayList<Course> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }

 
    public ArrayList<Course> getCompletedCourses() {
        return completedCourses;
    }

   
    public void setCompletedCourses(ArrayList<Course> completedCourses) {
        this.completedCourses = completedCourses;
    }

    public void showStudents() {
        for (Student student : admin.getStudents()) {
            students.add(student);
        }
    }
}
