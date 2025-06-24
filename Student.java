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
        this.studentId = Id;
        this.studentId = studentId;
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
        System.out.println("Enter student ID:");
        int id = sc.nextInt();
        sc.nextLine();
        professor.viewGrades(id);
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
        System.out.println("Enter student ID:");
        int inputId = sc.nextInt();
        sc.nextLine(); 

        for (Student student : students) {
            if (student.getId() == inputId) {
                this.studentId = inputId;
                break;
            }
        }

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
        System.out.println("Enter student ID:");
        int inputId = sc.nextInt();
        sc.nextLine(); 

       
        Student matchedStudent = null;
        for (Student student : students) {
            if (student.getId() == inputId) {
                matchedStudent = student;
                break;
            }
        }
        System.out.println("Enter current semester: ");
        int currSemesterInput = sc.nextInt();
        sc.nextLine(); 

      
        if (course.getSemester() == currSemesterInput) {
           
            if (course.enrollStudent(inputId)) {
                registeredCourses.add(course);  
                System.out.println("Registered for " + course.getTitle() + " for student ID: " + inputId);
            } else {
                System.out.println("Could not enroll.");
            }
        } else {
            throw new IllegalArgumentException("You can only register for courses in your current semester.");
        }
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid student ID and semester.");
        sc.nextLine(); 
    } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage()); 
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
    }
}
    public boolean prerequisiteMet(Course course) {
       
        return true;
    }

    public void viewGrades() {
        System.out.println("Enter student ID:");
        int inputId = sc.nextInt();
        sc.nextLine(); 

        for (Student student : students) {   
            if (student.getId() == inputId) {
                this.studentId = inputId;
                break;
            }
        }
    

        if (completedCourses.isEmpty()) {
            System.out.println("No courses completed yet for student ID: " + inputId);
        } else {
            System.out.println("Grades for Completed Courses (Student ID: " + inputId + "):");
            for (Course course : completedCourses) {
                System.out.println(course.getTitle() + " - Grade: A");
            }
        }
    }

    public void viewSchedule() {
        System.out.println("Enter student ID:");
        int inputId = sc.nextInt();
        sc.nextLine(); 

        for (Student student : students) {
            if (student.getId() == inputId) {
                this.studentId = inputId;
                break;
            }
        }
       

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
        System.out.println("Enter student ID:");
        int inputId = sc.nextInt();
        sc.nextLine(); 

        for (Student student : students) {
            if (student.getId() == inputId) {
                this.studentId = inputId;
                break;
            }
        }
      
        professor.trackProgress(this.studentId);
    }
    public void dropCourse(String courseCode, LocalDate dropDeadline) {
        try {
            System.out.println("Enter student ID:");
            int inputId = sc.nextInt();
            sc.nextLine(); 
    
            
            Student matchedStudent = null;
            for (Student student : students) {
                if (student.getId() == inputId) {
                    matchedStudent = student;
                    break;
                }
            }
            
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
