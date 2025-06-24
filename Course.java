import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseCode;
    private String title;
    private String professor;
    private int credits;
    private String prerequisites;
    private String schedule;
    private int semester;
    private List<Integer> enrolledStudents;
    private int capacity;

    public Course(String courseCode, String title, String professor, int credits, String prerequisites, String schedule, int semester, int capacity) {
        this.courseCode = courseCode;
        this.title = title;
        this.professor = professor;
        this.credits = credits;
        this.prerequisites = prerequisites;
        this.schedule = schedule;
        this.semester = semester;
        this.capacity = capacity;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getProfessor() {
        return professor;
    }

    public int getCredits() {
        return credits;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public String getSchedule() {
        return schedule;
    }

    public int getSemester() {
        return semester;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public boolean enrollStudent(int studentId) {
        if (enrolledStudents.size() < capacity) {
            enrolledStudents.add(studentId);
            System.out.println("Student " + studentId + " enrolled in " + title);
            return true;
        } else {

            System.out.println("Course " + title + " is full.");
            return false;
        }
    }
    public boolean isStudentEnrolled(int studentId) {
        return enrolledStudents.contains(studentId);
    }
    public int getEnrolledCount() {
        return enrolledStudents.size();
    }

    public void viewCourse() {
        System.out.println("Course Code: " + courseCode);
        System.out.println("Title: " + title);
        System.out.println("Professor: " + professor);
        System.out.println("Credits: " + credits);
        System.out.println("Prerequisites: " + prerequisites);
        System.out.println("Schedule: " + schedule);
        System.out.println("Semester: " + semester);
        System.out.println("Capacity: " + capacity);
        System.out.println("Enrolled: " + getEnrolledCount());
    }

    public String getGrade() {
        throw new UnsupportedOperationException("Unimplemented method 'getGrade'");
    }
}