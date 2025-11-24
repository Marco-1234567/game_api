package exercise_security.game_api.controller;

import exercise_security.game_api.dto.JwtResponseDTO;
import exercise_security.game_api.dto.LoginRequestDTO;
import exercise_security.game_api.security.JwtService;
import exercise_security.game_api.service.CustomUserDetailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class Controller {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final CustomUserDetailService customUserDetailsService;

    public Controller( AuthenticationManager authenticationManager, JwtService jwtService, CustomUserDetailService customUserDetailsService) {
        // TODO service
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO login){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.username(),
                        login.password()
                )
        );
        UserDetails user = customUserDetailsService.loadUserByUsername(login.username());

        String token = jwtService.genererateToken(user);

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
