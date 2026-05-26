package cat.itacademy.s04.t01.userapi.level1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public Status healthMessage(){
        return new Status("OK");
    }
}
