package exercise_security.game_api.service;

import exercise_security.game_api.dto.GameRequestDTO;
import exercise_security.game_api.dto.GameResponseDTO;
import exercise_security.game_api.dto.ReviewResponseDTO;
import exercise_security.game_api.exception.ResourceNotFoundException;
import exercise_security.game_api.model.Game;
import exercise_security.game_api.repository.GameRepository;
import exercise_security.game_api.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository repo;
    private final ReviewService reviewService;

    public GameService(GameRepository repo, ReviewRepository reviewRepo, ReviewService reviewService) {
        this.repo = repo;
        this.reviewService = reviewService;
    }

    public List<GameResponseDTO> getAll(){

        return repo.findAll().stream().map(this::toGameResponseDTO).toList();
    }

    public ResponseEntity<GameResponseDTO> getById(Long id) throws RuntimeException {

        if (repo.findById(id).isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(toGameResponseDTO( repo.findById(id).get()));
        } else {
            throw new ResourceNotFoundException("Can't find Game with id = " + id );
        }
    }

    public Optional<GameResponseDTO> addGame( GameRequestDTO requestDTO ){
        return Optional.of( toGameResponseDTO( repo.save( toGameEntity( requestDTO))));
    }

    public boolean deleteById(Long id){

        if (repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public GameResponseDTO updateGame(Long id, GameRequestDTO gameRequestDTO) {

        Optional<Game> existing = repo.findById(id);

        if (existing.isPresent()){
            existing.get().setTitle( gameRequestDTO.getTitle());
            existing.get().setGenre( gameRequestDTO.getGenre());
            existing.get().setReleaseYear( gameRequestDTO.getReleaseYear());
            repo.save(existing.get());
        return toGameResponseDTO(existing.get());

        } else {
            throw new ResourceNotFoundException("Can't find Game with id = " + id );
        }
    }

    ///
    /// only updating parts of the Game
    public GameResponseDTO patchGame(Long id,  GameRequestDTO gameRequestDTO){

        Optional<Game> existing = repo.findById(id);

        if (existing.isPresent()){
            if (gameRequestDTO.getTitle() != null){
                existing.get().setTitle( gameRequestDTO.getTitle());
            }

            if (gameRequestDTO.getGenre() != null){
                existing.get().setGenre( gameRequestDTO.getGenre());
            }

            if (gameRequestDTO.getReleaseYear() != null){
                existing.get().setReleaseYear( gameRequestDTO.getReleaseYear());
            }

            repo.save(existing.get());

            return toGameResponseDTO(existing.get());

        } else{
            throw new ResourceNotFoundException("Can't find Game with id = " + id );
        }
    }

    public List<GameResponseDTO> searchByTitle(String title){
        return repo.searchByTitle(title)
                .stream()
                .map(this::toGameResponseDTO).toList();
    }

    public Page<GameResponseDTO> searchByTitle2(String title, Pageable pageable){

        Page<Game> gamePage = repo.searchByTitle2(title, pageable);
        Page<GameResponseDTO> dto = gamePage.map(game1 -> toGameResponseDTO(game1));    // lambda expression
        return dto;                                                                           //
    }

    public Page<GameResponseDTO> searchByGenre(String genre, Pageable pageable){

        Page<Game> gamePage = repo.searchByGenre(genre, pageable);
        return gamePage.map(this::toGameResponseDTO);       //"non static" uses "this". (= method reference)
    }

    public Page<GameResponseDTO> getTopRatedGames(Pageable pageable){
        Page<Game> gamePage = repo.searchTopRatedGames(pageable);
        return gamePage.map(this::toGameResponseDTO);
    }

    protected Game toGameEntity(GameRequestDTO dto){
        return new Game(dto.getTitle(), dto.getGenre(), dto.getReleaseYear());
    }

    protected GameResponseDTO toGameResponseDTO(Game game){

        Set<ReviewResponseDTO> reviews = game.getReviews() != null
                ? game.getReviews().stream().map(reviewService::toReviewResponseDTO).collect(Collectors.toSet())
                : Set.of();

        return new GameResponseDTO( game.getTitle(), game.getGenre(), game.getReleaseYear(), reviews);
    }
}