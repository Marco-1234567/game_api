package exercise_security.game_api.model;

import jakarta.persistence.*;

@Entity
@Table(name="reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String review;

    private Integer rating;

    @ManyToOne
    @JoinColumn(name="game_id")
    private Game game;

    public Review() {
    }

    public Review(String review, Integer rating) {
        this.review = review;
        this.rating = rating;
    }

    public Review(Long id, String review, Integer rating) {
        this.id = id;
        this.review = review;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}