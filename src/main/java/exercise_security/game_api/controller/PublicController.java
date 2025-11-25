package exercise_security.game_api.controller;

import exercise_security.game_api.dto.GameResponseDTO;
import exercise_security.game_api.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/games")
public class PublicController {

    private final GameService service;


    public PublicController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public List<GameResponseDTO> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> getById(@PathVariable Long id){
        return service.getById(id).map(game -> ResponseEntity.ok(game)).orElse(ResponseEntity.status(418).build());
    }


}
