package exercise_security.game_api.repository;

import exercise_security.game_api.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class GameRepositoryJPATest {

    @Autowired          // instead of dependency injection
    GameRepository gameRepository;

    @BeforeEach
    void setup(){
        // arrange
        Game g1 = new Game("Game 1", "Horror", 2001);
        Game g2 = new Game("Game 2", "Horror", 2002);
        Game g3 = new Game("Game 3", "Comic", 2020);
        gameRepository.save(g1);
        gameRepository.save(g2);
        gameRepository.save(g3);
    }

    @Test
    @DisplayName("Find game by genre. Should return a list of games")
    void findByGenre(){

        // act
        List<Game> result = gameRepository.findByGenre("Horror");

        // assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Game::getTitle).containsExactlyInAnyOrder("Game 1", "Game 2");
    }

    @Test
    @DisplayName("Search by title should return a list of games")
    void searchByTitle(){

        // act
        List<Game> result = gameRepository.searchByTitle("3");

        // assert
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Game::getTitle).containsExactlyInAnyOrder("Game 3");
    }

    @Test
    @DisplayName("Save incomplete game. Should throw exception.")
    void saveGame(){

        // arrange
        Game g4 = new Game();

        // act + assert
        assertThrows(
                DataIntegrityViolationException.class,
                () -> gameRepository.saveAndFlush(g4)   //  saveandflush för att skcika sql direkt, så att constraint triggas
        );
    }
}