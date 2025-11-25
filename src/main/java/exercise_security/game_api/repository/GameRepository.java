package exercise_security.game_api.repository;

import exercise_security.game_api.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // Kom ihåg!
    // public behövs ej. Allt är public i ett interface
    // Använd Model/Entity namn i Query. Inte databasens tabellnamn. (JPA vill ha det så)

    @Query("SELECT g FROM Game g WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Game> searchByTitle(@Param("keyword") String keyword);

    @Query("SELECT g FROM Game g WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Game> searchByTitle2(@Param("keyword") String keyword, Pageable pageable);

}
