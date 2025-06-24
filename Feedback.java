public class Feedback <T,U>{

    private T rating;
    private U feedback;

    public Feedback(T rating, U feedback) {
        this.rating = rating;
        this.feedback = feedback;
    }

    public String showRating() {
        return "Rating: " + rating;
    }

    public String showFeedback() {
        return "Feedback: " + feedback;
    }
}