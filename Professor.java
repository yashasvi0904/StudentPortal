// Professor class
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Professor extends User {
    private String name;
    private List<Course> assignedCourses;
    private Map<Integer, List<String>> studentCourseCompletion; 
    private Map<Integer, Map<String, String>> studentGrades; 
    private FeedbackSystem<Integer, String> feedbackSystem;
    private List<Student> students;
    private Admin admin;

    public Professor(String email, String password, String name, FeedbackSystem<Integer, String> feedbackSystem , Admin admin) {
        super(email, password);
        this.name = name;
        this.assignedCourses = new ArrayList<>();
        this.studentCourseCompletion = new HashMap<>();
        this.studentGrades = new HashMap<>();
        this.feedbackSystem = feedbackSystem;
        this.students = new ArrayList<>();
        this.admin = admin;
    }
 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCourse(Course course) {
        assignedCourses.add(course);
        // System.out.println("Course " + course.getCourseCode() + " added to assigned courses.");
    }

    public void removeCourse(String courseCode) {
        assignedCourses.removeIf(course -> course.getCourseCode().equals(courseCode));
    }

    public void viewAssignedCourses() {
        if (assignedCourses.isEmpty()) {
            System.out.println("No courses assigned.");
        } else {
            System.out.println("Assigned Courses:");
            for (Course course : assignedCourses) {
                course.viewCourse();
                System.out.println();
            }
        }
    }

    public void viewFeedback() {
        System.out.println("Feedback for Professor:");
        feedbackSystem.viewFeedback();
    }

    public void trackProgress(int studentId) {
        Map<String, String> grades = studentGrades.get(studentId);
        if (grades != null && !grades.isEmpty()) {
            System.out.println("Progress for student ID " + studentId + ":");
            double totalGradePoints = 0;
            int totalCredits = 0;
            
            for (Map.Entry<String, String> entry : grades.entrySet()) {
                String courseCode = entry.getKey();
                String grade = entry.getValue();
                Course course = getCourseByCode(courseCode);
                if (course != null) {
                    int credits = course.getCredits();
                    double gradePoints = getGradePoints(grade);
                    totalGradePoints += gradePoints * credits;
                    totalCredits += credits;
                    System.out.println(course.getTitle() + " - Grade: " + grade + ", Credits: " + credits);
                }
            }
            
            if (totalCredits > 0) {
                double gpa = totalGradePoints / totalCredits;
                System.out.println("Current GPA: " + String.format("%.2f", gpa));
            } else {
                System.out.println("No completed courses yet.");
            }
        } else {
            System.out.println("No grades available for student ID " + studentId);
        }
    }
    private Course getCourseByCode(String courseCode) {
        for (Course course : assignedCourses) {
            if (course.getCourseCode().equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    private double getGradePoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    public void markCourseCompleted(int studentId, String courseCode) {
        List<Student> studentL = admin.getStudents();
        for (int i = 0; i < studentL.size(); i++) {
            if (studentId == studentL.get(i).getId()) {
                studentCourseCompletion.computeIfAbsent(studentId, k -> new ArrayList<>()).add(courseCode);
                System.out.println("Course " + courseCode + " marked as completed for student ID " + studentId);
                return;
            }
        }
        System.out.println("Invalid student ID. Please try again.");
    }

    public void setGrade(int studentId, String courseCode, String grade) {
        studentGrades.computeIfAbsent(studentId, k -> new HashMap<>()).put(courseCode, grade);
        System.out.println("Grade " + grade + " set for student ID " + studentId + " in course " + courseCode);
    }

    public void viewGrades(int studentId) {
        if (studentGrades.containsKey(studentId)) {
            System.out.println("Grades for student ID " + studentId + ":");
            studentGrades.get(studentId).forEach((courseCode, grade) ->
                    System.out.println("Course: " + courseCode + ", Grade: " + grade));
        } else {
            System.out.println("No grades available for student ID " + studentId);
        }
    }

    public void viewCompletedCourses(int studentId) {
        if (studentCourseCompletion.containsKey(studentId)) {
            System.out.println("Completed courses for student ID " + studentId + ":");
            studentCourseCompletion.get(studentId).forEach(courseCode -> System.out.println("Course: " + courseCode));
        } else {
            System.out.println("No completed courses for student ID " + studentId);
        }
    }

    public Map<Integer, Map<String, String>> getStudentGrades() {
        return studentGrades;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }
}