package cat.itacademy.s04.t01.userapi.level2.repositories;

import cat.itacademy.s04.t01.userapi.level2.model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryUserRepositoryTest {
    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();

    @Test
    void findAll_returnsEmptyListInitially(){
        List<User> result = userRepository.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void findAll_neverReturnsNull(){
        assertThat(userRepository.findAll()).isNotNull();
    }

    @Test
    void save_addUserToList(){
        User user = new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com");
        userRepository.save(user);
        assertThat(userRepository.findAll()).contains(user);
    }

    @Test
    void findAll_returnAllSavesUsers(){
        User user1 = new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com");
        User user2 = new User(UUID.randomUUID(), "Pablo Sola", "pablo@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> result = userRepository.findAll();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(user1, user2);
    }

    @Test
    void findById_returnsUserIfExists(){
        User user = new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com");
        userRepository.save(user);
        assertThat(userRepository.findById(user.getId())).contains(user);
    }

    @Test
    void findById_returnsEmptyIfNotExists(){
        assertThat(userRepository.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void searchByName_filtersCorrectly(){
        User user1 = new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com");
        User user2 = new User(UUID.randomUUID(), "Joan Sola", "pablo@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        var result = userRepository.searchByName("jo");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Joan Sola");
    }


    @Test
    void existsByEmail_returnsTrueIfEmailExists(){
        User user1 = new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com");

        userRepository.save(user1);

        assertThat(userRepository.existsByEmail("ada@example.com")).isTrue();
    }
}
