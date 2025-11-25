package exercise_security.game_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ReviewRequestDTO {

    @NotBlank(message = "Must leave a review comment.")
    private  String review;

    @Min(value = 1, message = "Rating, minimum is 1.")
    @Max(value = 10, message = "Rating, maximum is 10.")
    private Integer rating;

    public ReviewRequestDTO(String review, Integer rating) {
        this.review = review;
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
