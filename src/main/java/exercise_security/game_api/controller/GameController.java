package exercise_security.game_api.controller;

import exercise_security.game_api.dto.GameRequestDTO;
import exercise_security.game_api.dto.GameResponseDTO;
import exercise_security.game_api.dto.ReviewRequestDTO;
import exercise_security.game_api.dto.ReviewResponseDTO;
import exercise_security.game_api.exception.ResourceNotFoundException;
import exercise_security.game_api.service.GameService;
import exercise_security.game_api.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService service;
    private final ReviewService reviewService;

    public GameController(GameService service, ReviewService reviewService) {
        this.service = service;
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<GameResponseDTO> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> getById(@Valid @PathVariable Long id){

        return service.getById(id);
                //.map(game -> ResponseEntity.ok(game))
                //.orElseThrow( () -> new ResourceNotFoundException("Can't find Game with id = " + id ));
    }

    @PostMapping
    public ResponseEntity<GameResponseDTO> addGame( @Valid @RequestBody GameRequestDTO gameRequestDTO){
        return service.addGame(gameRequestDTO).map(game -> ResponseEntity.ok(game)).orElse(ResponseEntity.status(418).build());
    }

    @PostMapping("/review/{gameId}")
    public ResponseEntity<ReviewResponseDTO> addReview(@PathVariable Long gameId, @Valid @RequestBody ReviewRequestDTO reviewRequestDTO){
        return reviewService.addReview(gameId, reviewRequestDTO).map( review -> ResponseEntity.ok(review))
                .orElse(ResponseEntity.status(418).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDTO> updateGame(@PathVariable Long id, @Valid @RequestBody GameRequestDTO gameRequestDTO){
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

    @GetMapping("/search")
    public List<GameResponseDTO> searchByTitle(@RequestParam String title ){

        return service.searchByTitle(title);
    }

    @GetMapping("/search2")
    public ResponseEntity<Page<GameResponseDTO>> searchByTitle2(@RequestParam String title, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int pageSize ){

        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(service.searchByTitle2(title, pageable));
    }
}
