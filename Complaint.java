public class Complaint {
    private static int idCounter = 1;
    private int id;
    private int studentId;
    private String description;
    private String status;

    public Complaint(String description, int studentId) {
        this.id = idCounter++;
        this.description = description;
        this.studentId = studentId;
        this.status = "Pending";
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}