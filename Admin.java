import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Admin extends User {
    private List<Course> courses;
    private List<Professor> professors;
    private List<Student> students;
    private ComplaintSystem complaintSystem;
    

    public Admin(String email, String password) {
        super(email, password);
        this.courses = new ArrayList<>();
        this.professors = new ArrayList<>();
        this.students = new ArrayList<>();
        this.complaintSystem = new ComplaintSystem();
    }

    
    public void addStudent(Student student) {
        students.add(student);
       
    }
    

    public void removeStudent(int studentId) {
        students.removeIf(student -> student.getId() == studentId);
        System.out.println("Student removed with ID: " + studentId);
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student findStudentById(int studentId) {
        for (Student student : students) {
            if (student.getId() == studentId) {
                return student;
            }
        }
                return null;
    }

    public void addCourse(Course course) {
        courses.add(course);
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
    public boolean isCoursePresent(String code) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
    public void removeCourse(String code) {
        courses.removeIf(course -> course.getCourseCode().equals(code));
        System.out.println("Course removed with code: " + code);
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addProfessor(Professor professor) {
        professors.add(professor);
    }

    public void removeProfessor(String name) {
        professors.removeIf(professor -> professor.getName().equals(name));
        System.out.println("Professor removed with name: " + name);
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessor(String oldName, String newName) {
        for (Professor professor : professors) {
            if (professor.getName().equals(oldName)) {
                professor.setName(newName);
                System.out.println("Professor's name changed from " + oldName + " to " + newName);
                return;
            }
        }
        System.out.println("Professor with name " + oldName + " not found.");
    }

    public void assignCourseToProfessor(String courseCode, Professor professor) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                professor.addCourse(course);
                course.setProfessor(professor.getName());
                System.out.println("Course " + courseCode + " assigned to Professor " + professor.getName());
                return;
            }
        }
        System.out.println("Course with code " + courseCode + " not found.");
    }

    public ComplaintSystem getComplaintSystem() {
        return complaintSystem;
    }

    public void viewComplaints() {
        System.out.println("Viewing all complaints:");
        complaintSystem.viewComplaints();
    }

    public void resolveComplaint(int id) {
        complaintSystem.resolveComplaint(id);
    }
}