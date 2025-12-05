package exercise_security.game_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise_security.game_api.model.Game;
import exercise_security.game_api.repository.GameRepository;
import exercise_security.game_api.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Deprecated in jdk21 @MockBean replacement @MockitoBean
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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
    }

    @Test
    @DisplayName("Test Game getById() returning a game")
    void testGetById() throws Exception {

        // arrange: use repository directly in arrange. (Service layer may contain errors).
        Game game_1 = new Game("Game 1", "Horror", 2011);
        Long index = gameRepository.save(game_1).getId();

        // act + assert
        mockMvc.perform(get("/games/" + index))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Game 1"))
                .andExpect(jsonPath("$.genre").value("Horror"));
    }

    @Test
    @DisplayName("Test AddGame() http response 201")
    void testPOSTAddGame() throws Exception {
        // arrange
        Game input = new Game("Game 1", "Horror", 2011);
        // without objectmapper
        //String sInput = "{\n\"title\":\"Game 1\",\n \"genre\":\"Horror\",\n \"releaseYear\": 2011 \n}";

        // act + assert
        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect( status().isCreated())
                .andExpect(jsonPath("$.title").value("Game 1"))
                .andExpect(jsonPath("$.genre").value("Horror"))
                .andExpect(jsonPath("$.releaseYear").value("2011"));
    }
}