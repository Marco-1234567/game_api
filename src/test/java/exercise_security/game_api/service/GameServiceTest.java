package exercise_security.game_api.service;

import exercise_security.game_api.dto.GameRequestDTO;
import exercise_security.game_api.dto.GameResponseDTO;
import exercise_security.game_api.exception.ResourceNotFoundException;
import exercise_security.game_api.model.Game;
import exercise_security.game_api.repository.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private GameResponseDTO gameResponseDTO;
    private Game game;

    @BeforeEach
    void setUp() {
        gameResponseDTO = new GameResponseDTO();
        gameResponseDTO.setTitle("Game 1");
        gameResponseDTO.setGenre("Horror");
        gameResponseDTO.setReleaseYear(2001);

        game = new Game("Game 1", "Horror", 2001);
    }

    @Mock
    GameRepository gameRepository;

    @Spy
    @InjectMocks
    GameService gameService;

    // public ResponseEntity<GameResponseDTO> getById(Long id)
    @Test
    @DisplayName("Test service.getById() Shall return ResponseEntity")
    void getById(){

        // arrange: mock operations
        when(gameRepository.findById(1L)).thenReturn( Optional.of(game));
        doReturn(gameResponseDTO).when(gameService).toGameResponseDTO(game);

        //act
        ResponseEntity<GameResponseDTO> result = gameService.getById(1L);

        // assert
        assertNotNull(result.getBody());
        assertEquals("Game 1",result.getBody().getTitle());
        verify(gameRepository, times(2)).findById(1L);
        verify(gameService, times(1)).toGameResponseDTO(game);
    }

    @Test
    @DisplayName("Test service.getById() Shall return ResourceNotFoundException")
    void testResourceNotFoundWhenGettingById(){

        // arrange: mock operations
        when(gameRepository.findById(1L)).thenReturn( Optional.empty());

        //act
        ResourceNotFoundException resultException = assertThrows(ResourceNotFoundException.class, () -> gameService.getById(1L));

        // assert
        assertNotNull(resultException);
        assertEquals("Can't find Game with id = 1", resultException.getMessage());
        verify(gameRepository, times(1)).findById(1L);
    }

    // public Optional<GameResponseDTO> addGame( GameRequestDTO requestDTO ){
    @Test
    @DisplayName("Run service.create and verify repo.save is used")
    void addGame(){
        // arrange
        GameRequestDTO gameRequestDTO = new GameRequestDTO();
        gameRequestDTO.setTitle("Game 1");
        gameRequestDTO.setGenre("Horror");
        gameRequestDTO.setReleaseYear(2001);

        // mocka returdata från resursmetoder vi använder.
        when(gameRepository.save(game)).thenReturn(game);

        // annan mocksyntax för metoder i det vi spionerar på (@Spy on service)
        doReturn(gameResponseDTO).when(gameService).toGameResponseDTO(game);
        doReturn(game).when(gameService).toGameEntity(gameRequestDTO);

        //act
        Optional<GameResponseDTO> result = gameService.addGame(gameRequestDTO);

        // assert
        assertTrue(result.isPresent());
        assertEquals("Game 1",result.get().getTitle());
        verify(gameRepository, times(1)).save(game);
        verify(gameService, times(1)).toGameResponseDTO(game);
        verify(gameService, times(1)).toGameEntity(gameRequestDTO);
    }

    // exercise 6
    @Test
    @DisplayName("Test deleteById() is working properly")
    void testDeleteGameById(){
        // arrange: mocka returdata från resursmetoder vi använder.
        // deleteById() returnerar void, och det går inte att styra "happy path" vid returtyp void.
        // Lösning: kör testet (act) och verifiera sedan att metoden har anropats
        //when(gameRepository.deleteById(1L)).thenReturn();

        //act
        boolean result = gameService.deteteById(1L);

        // assert
        verify(gameRepository, times(1)).deleteById(1L);
        verify(gameRepository, never()).save(game);
    }
}