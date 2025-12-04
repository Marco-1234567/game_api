package exercise_security.game_api.controller;

import exercise_security.game_api.dto.GameRequestDTO;
import exercise_security.game_api.repository.GameRepository;
import exercise_security.game_api.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Depricated in jdk21 @MockBean replacement @MockitoBean
//@WebMvcTest(GameController.class)
// I'm using @Autowired instead of @MockBean, and @SpringBootTest and not @WebMvcTest(GameController.class)

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
    }

    @Test
    @DisplayName("Test Game getById() returning a game")
    void testGetById() throws Exception {

        // arrange
        GameRequestDTO gameRequestDTO = new GameRequestDTO("Game 1", "Horror", 2011);
        gameService.addGame(gameRequestDTO);

        // act + assert
        mockMvc.perform(get("/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Game 1"))
                .andExpect(jsonPath("$.genre").value("Horror"));
    }
}