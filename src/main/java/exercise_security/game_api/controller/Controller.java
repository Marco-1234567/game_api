package exercise_security.game_api.controller;

import exercise_security.game_api.dto.JwtDTO;
import exercise_security.game_api.dto.JwtResponseDTO;
import exercise_security.game_api.dto.LoginRequestDTO;
import exercise_security.game_api.model.AppUser;
import exercise_security.game_api.model.Role;
import exercise_security.game_api.repository.UserRepository;
import exercise_security.game_api.security.JwtService;
import exercise_security.game_api.service.CustomUserDetailService;
import exercise_security.game_api.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class Controller {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final CustomUserDetailService customUserDetailsService;

    public Controller(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, CustomUserDetailService customUserDetailsService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequestDTO){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.username(),
                        loginRequestDTO.password()
                )
        );
        UserDetails user = customUserDetailsService.loadUserByUsername(loginRequestDTO.username());

        // todo? handle in service layer? like a login service?
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        AppUser userToUpdate = userService.getUserByName(user.getUsername());
        userToUpdate.setRefreshToken(refreshToken);
        userService.saveUser(userToUpdate);

        //return ResponseEntity.ok(new JwtDTO(accessToken, refreshToken));
        return accessToken;

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken)
    {
        Claims claims;

        try
        {
            claims = jwtService.extractAllClaims(refreshToken);
        }catch (Exception e)
        {
            return ResponseEntity.status(403).body("Invalid refresh token");
        }

        String username = claims.getSubject();
        UserDetails user = customUserDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new JwtDTO(newAccessToken, refreshToken));
    }
}
