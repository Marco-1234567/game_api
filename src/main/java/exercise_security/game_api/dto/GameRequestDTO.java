package exercise_security.game_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class GameRequestDTO {

    @NotBlank
    @Size(min=4)
    private String title;

    @NotBlank
    @Size(min=4, message = "Genre must have 4 chars at least")
    @Size(max=40, message = "Maximum 40 characters in genre.")
    private String genre;

    @Min(value = 1, message = "Rating, minimum 1.")
    @Max(value = 10, message = "Rating, maximum 10.")
    private Integer rating;

    @Min(1900)
    private Integer releaseYear;

    public GameRequestDTO(String title, String genre, Integer rating, Integer releaseYear) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}
