import java.util.ArrayList;
import java.util.List;


public class FeedbackSystem <T,U>{
    private List<Feedback<T,U>> feedbackList = new ArrayList<>();

    public void addFeedback(Feedback<T,U> feedback) {
        feedbackList.add(feedback);
    }

    public void viewFeedback() {
        if (feedbackList.isEmpty()) {
            System.out.println("No feedback available.");
        } else {
            for (Feedback<T,U> feedback : feedbackList) {
                System.out.println(feedback.showRating());
                System.out.println(feedback.showFeedback());
            }
        }
    }
}