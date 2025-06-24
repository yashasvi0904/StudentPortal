public class TeachingAssistant extends Student {
    public TeachingAssistant(String email, String password, int id, int semester, int credits, int coursesCompleted, int gpa, FeedbackSystem<Integer, String> feedbackSystem ,  Professor professor) {
        super(email, password, id, semester, credits, coursesCompleted, gpa, feedbackSystem , professor , null);
    }
    public void assistProfessor(Professor professor) {
        System.out.println("Assisting Professor " + professor.getName());
    }
    public void viewGrades(Professor professor, int studentId) {
        professor.viewGrades(studentId);
    }

    public void markCourseCompleted(Professor professor, int studentId, String courseCode) {
        professor.markCourseCompleted(studentId, courseCode);
    }
    public void setGrade(Professor professor, int studentId, String courseCode, String grade) {
        professor.setGrade(studentId, courseCode, grade);
    }
    public void viewCompletedCourses(Professor professor, int studentId) {
        professor.viewCompletedCourses(studentId);
    }
}