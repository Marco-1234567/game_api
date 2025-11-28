package exercise_security.game_api.service;

import exercise_security.game_api.dto.AppUserResponseDTO;
import exercise_security.game_api.model.AppUser;
import exercise_security.game_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUserResponseDTO>getAllUsers(){
        return userRepository.findAll().stream().map(user -> toAppUserResponseDTO(user)).toList();
    }

    private AppUserResponseDTO toAppUserResponseDTO( AppUser appUser){
        return new AppUserResponseDTO( appUser.getUsername(), appUser.getPassword(), appUser.getRoles() );
    }

    public AppUser getUserByName(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    public void saveUser(AppUser user){
        AppUser responseUser = userRepository.save(user);
        // todo handle response? throw exception?
    }
}
