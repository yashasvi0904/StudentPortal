import java.util.ArrayList;
import java.util.List;

public class ComplaintSystem {
    private List<Complaint> complaints;

    public ComplaintSystem() {
        this.complaints = new ArrayList<>();
    }

    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
        System.out.println("Complaint added with ID: " + complaint.getId() + " for student ID: " + complaint.getStudentId());
    }

    public void viewComplaints() {
        if (complaints.isEmpty()) {
            System.out.println("No complaints to display.");
        } else {
            System.out.println("Complaints:");
            for (Complaint complaint : complaints) {
                System.out.println("ID: " + complaint.getId() + ", Student ID: " + complaint.getStudentId() + 
                                   ", Description: " + complaint.getDescription() + ", Status: " + complaint.getStatus());
            }
        }
    }

    public void resolveComplaint(int id) {
        for (Complaint complaint : complaints) {
            if (complaint.getId() == id) {
                complaint.setStatus("Resolved");
                System.out.println("Complaint with ID " + id + " for student ID " + complaint.getStudentId() + " has been resolved.");
                return;
            }
        }
        System.out.println("Complaint with ID " + id + " not found.");
    }
}