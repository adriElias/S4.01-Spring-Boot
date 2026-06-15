package cat.itacademy.s04.t01.userapi.level2andLevel3.services;

import cat.itacademy.s04.t01.userapi.level2andLevel3.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String name, String email);
    List<User> getAllUsers();
    User getById(UUID id);
    List<User> searchByName(String name);
}
