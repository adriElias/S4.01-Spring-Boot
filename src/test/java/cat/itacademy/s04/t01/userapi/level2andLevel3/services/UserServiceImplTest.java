package cat.itacademy.s04.t01.userapi.level2andLevel3.services;

import cat.itacademy.s04.t01.userapi.level2andLevel3.exceptions.EmailAlreadyExistsException;
import cat.itacademy.s04.t01.userapi.level2andLevel3.exceptions.UserNotFoundException;
import cat.itacademy.s04.t01.userapi.level2andLevel3.model.User;
import cat.itacademy.s04.t01.userapi.level2andLevel3.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// Indiquem a JUnit que utilitzi l’extensió de Mockito.
// Això permet que les anotacions @Mock i @InjectMocks funcionin.
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    // Simulem el repositori. Així no cal base de dades real.
    @Mock
    private UserRepository userRepository;

    // Creem una instància real de la classe a provar (UserServiceImpl).
    // Els mocks definits a dalt s’injectaran aquí automàticament.
    @InjectMocks
    private UserServiceImpl userService;


    // ERROR PATH
    // Volem comprovar què passa quan l’email ja existeix.
    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        // GIVEN:
        // - El repositori retorna true quan comprovem si existeix l’email
        when(userRepository.existsByEmail("ada@example.com")).thenReturn(true);

        // WHEN:
        // - Intentem crear un usuari amb aquest email usant el Servei
        assertThrows(EmailAlreadyExistsException.class, () ->
                userService.createUser("Ada", "ada@example.com"));

        // THEN:
        // - Comprovar que es llança una excepció EmailAlreadyExistsException
        // - Verificar que NO s’ha cridat al mètode save() del repository
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldSaveUserWhenEmailDoesNotExist(){
        // GIVEN
        when(userRepository.existsByEmail("ada@example.com")).thenReturn(false);

        // Simulamos que save() devuelve el usuario que recibe
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        User result = userService.createUser("Ada Lovelace", "ada@example.com");

        // THEN
        assertNotNull(result.getId());
        assertEquals("Ada Lovelace", result.getName());
        assertEquals("ada@example.com", result.getEmail());

        // Verificamos que se llamó a save()
        verify(userRepository).save(any());
    }

    @Test
    void getById_shouldReturnUserWhenExists() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Ada Lovelace", "ada@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getById(id);

        assertEquals(user, result);
    }

    @Test
    void getById_shouldThrowExceptionWhenUserNotFound() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getById(id));
    }

    @Test
    void getAllUsers_shouldReturnListFromRepository() {
        List<User> users = List.of(
                new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com"),
                new User(UUID.randomUUID(), "Eduard Sola", "eduard@example.com")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(users, result);
    }

    @Test
    void searchByName_shouldReturnAllUsersWhenNameIsNull() {
        List<User> users = List.of(
                new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.searchByName(null);

        assertEquals(users, result);
    }
}
