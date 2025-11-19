package exercise_security.game_api.controller;

import exercise_security.game_api.dto.AppUserResponseDTO;
import exercise_security.game_api.model.AppUser;
import exercise_security.game_api.service.UserService;
import jakarta.persistence.Table;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponseDTO>> getALlUsers(){
        return ResponseEntity.ok(service.getAllUsers());
    }
}
