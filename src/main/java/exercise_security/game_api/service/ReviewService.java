package exercise_security.game_api.service;

import exercise_security.game_api.dto.ReviewRequestDTO;
import exercise_security.game_api.dto.ReviewResponseDTO;
import exercise_security.game_api.model.Game;
import exercise_security.game_api.model.Review;
import exercise_security.game_api.repository.GameRepository;
import exercise_security.game_api.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository repo;
    private final GameRepository gameRepo;

    public ReviewService(ReviewRepository repo, GameRepository gameRepo) {
        this.repo = repo;
        this.gameRepo = gameRepo;
    }

    public Optional<ReviewResponseDTO> addReview( Long gameId, ReviewRequestDTO reviewRequestDTO){
        // adding a Game to review (not a gameId, JPA takes care of that)
        Game game = gameRepo.findById(gameId).orElseThrow( () -> new RuntimeException("Game not found."));

        Review review = toReviewEntity(reviewRequestDTO);
        review.setGame(game);

        return Optional.of( toReviewResponseDTO( repo.save( review)));
    }

    private Review toReviewEntity( ReviewRequestDTO reviewRequestDTO){

        return new Review( reviewRequestDTO.getReview(), reviewRequestDTO.getRating());
    }

    public ReviewResponseDTO toReviewResponseDTO( Review review ){
        return new ReviewResponseDTO(review.getReview(), review.getRating());
    }
}
