package exercise_security.game_api.controller;

import exercise_security.game_api.dto.GameRequestDTO;
import exercise_security.game_api.dto.GameResponseDTO;
import exercise_security.game_api.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public List<GameResponseDTO> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> getById(@Valid @PathVariable Long id){
        return service.getById(id).map(game -> ResponseEntity.ok(game)).orElse(ResponseEntity.status(418).build());
    }

    @PostMapping
    public ResponseEntity<GameResponseDTO> addGame( @Valid @RequestBody GameRequestDTO gameRequestDTO){
        return service.addGame(gameRequestDTO).map(game -> ResponseEntity.ok(game)).orElse(ResponseEntity.status(418).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDTO> updateGame(@PathVariable Long id, @RequestBody GameRequestDTO gameRequestDTO){
        GameResponseDTO response = service.updateGame(id, gameRequestDTO);

        if (response != null){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@Valid @PathVariable Long id){
        boolean result = service.deteteById(id);
        return result ? ResponseEntity.ok("yes!") : ResponseEntity.notFound().build();
    }
}
