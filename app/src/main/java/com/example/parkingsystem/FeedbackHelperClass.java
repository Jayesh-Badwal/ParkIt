package com.example.parkingsystem;

public class FeedbackHelperClass {
    private int rating;
    private String review;

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}
