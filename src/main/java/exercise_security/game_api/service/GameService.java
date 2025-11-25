package exercise_security.game_api.service;

import exercise_security.game_api.dto.GameRequestDTO;
import exercise_security.game_api.dto.GameResponseDTO;
import exercise_security.game_api.model.Game;
import exercise_security.game_api.repository.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository repo;

    public GameService(GameRepository repo) {
        this.repo = repo;
    }

    public List<GameResponseDTO> getAll(){

        return repo.findAll().stream().map(g -> toGameResponseDTO(g)).toList();
    }

    public Optional<GameResponseDTO> getById(Long id){

        if (repo.findById(id).isPresent()){
            return Optional.of( toGameResponseDTO( repo.findById(id).get()));
        } else {
            return Optional.empty();
        }
    }

    public Optional<GameResponseDTO> addGame( GameRequestDTO requestDTO ){
        return Optional.of( toGameResponseDTO( repo.save( toGameEntity( requestDTO))));
    }

    public boolean deteteById(Long id){

        if (repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public GameResponseDTO updateGame(Long id,  GameRequestDTO gameRequestDTO){

        Optional<Game> existing = repo.findById(id);

        if (existing.isPresent()){
            existing.get().setTitle( gameRequestDTO.getTitle());
            existing.get().setGenre( gameRequestDTO.getGenre());
            existing.get().setReleaseYear( gameRequestDTO.getReleaseYear());
            repo.save(existing.get());
        return toGameResponseDTO(existing.get());
        }

        return null;
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
        }

        return null;
    }

    public List<GameResponseDTO> searchByTitle(String title){
        return repo.searchByTitle(title)
                .stream()
                .map(this::toGameResponseDTO).toList();
    }

    public Page<GameResponseDTO> searchByTitle2(String title, Pageable pageable){

        Page<Game> gamePage = repo.searchByTitle2(title, pageable);

        Page<GameResponseDTO> dto = gamePage.map(game1 -> toGameResponseDTO(game1));
        return dto;
    }

    private Game toGameEntity(GameRequestDTO dto){
        return new Game(dto.getTitle(), dto.getGenre(), dto.getReleaseYear());
    }

    private GameResponseDTO toGameResponseDTO(Game game){
        return new GameResponseDTO( game.getTitle(), game.getGenre(), game.getReleaseYear());
    }
}