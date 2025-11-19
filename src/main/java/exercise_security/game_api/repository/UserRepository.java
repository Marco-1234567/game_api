package exercise_security.game_api.repository;

import exercise_security.game_api.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    public Optional<AppUser> findByUsername(String username);
}
