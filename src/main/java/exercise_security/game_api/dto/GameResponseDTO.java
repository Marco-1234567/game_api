package exercise_security.game_api.dto;

import java.time.LocalDate;
import java.util.Set;

public class GameResponseDTO {

    private String title;
    private String genre;
    private Integer releaseYear;
    private Set<ReviewResponseDTO> reviewResponseDTO;

    public GameResponseDTO(String title, String genre, Integer releaseYear, Set<ReviewResponseDTO> reviewResponseDTO) {
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.reviewResponseDTO = reviewResponseDTO;
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

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Set<ReviewResponseDTO> getReviewResponseDTO() {
        return reviewResponseDTO;
    }

    public void setReviewResponseDTO(Set<ReviewResponseDTO> reviewResponseDTO) {
        this.reviewResponseDTO = reviewResponseDTO;
    }
}