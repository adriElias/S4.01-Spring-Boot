package cat.itacademy.s04.t01.userapi.level2andLevel3.controller;

import cat.itacademy.s04.t01.userapi.level2andLevel3.dto.CreateUserRequest;
import cat.itacademy.s04.t01.userapi.level2andLevel3.model.User;
import cat.itacademy.s04.t01.userapi.level2andLevel3.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(required = false) String name) {
        return userService.searchByName(name);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.name(), request.email());
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getById(id);
    }
}
