package cat.itacademy.s04.t01.userapi.level2andLevel3.services;

import cat.itacademy.s04.t01.userapi.level2andLevel3.exceptions.UserNotFoundException;
import cat.itacademy.s04.t01.userapi.level2andLevel3.model.User;
import cat.itacademy.s04.t01.userapi.level2andLevel3.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

public class UserServiceImp implements UserService{

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(UUID.randomUUID(), name, email);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    @Override
    public List<User> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return userRepository.findAll();
        }

        return userRepository.searchByName(name);
    }
}
