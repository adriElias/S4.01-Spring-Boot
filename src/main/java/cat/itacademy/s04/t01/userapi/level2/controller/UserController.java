package cat.itacademy.s04.t01.userapi.level2.controller;

import cat.itacademy.s04.t01.userapi.level1.Status;
import cat.itacademy.s04.t01.userapi.level2.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return users;
    }

}
