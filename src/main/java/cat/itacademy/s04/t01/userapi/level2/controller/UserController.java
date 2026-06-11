package cat.itacademy.s04.t01.userapi.level2.controller;

import cat.itacademy.s04.t01.userapi.level1.Status;
import cat.itacademy.s04.t01.userapi.level2.dto.CreateUserRequest;
import cat.itacademy.s04.t01.userapi.level2.model.User;
import cat.itacademy.s04.t01.userapi.level2.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
public class UserController {
    public List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getAllUsers(@RequestParam(required = false) String name) {
        if(name == null || name.isBlank()){
            return users;
        }

        return users.stream()
                .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody CreateUserRequest userRequest){
        UUID id =UUID.randomUUID();
        User user = new User(id, userRequest.name(), userRequest.email());
        users.add(user);
        return user;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id){
        return users.stream()
                .filter(user -> user.getId().toString().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
