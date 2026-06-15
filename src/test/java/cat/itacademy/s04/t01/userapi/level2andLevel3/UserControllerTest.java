package cat.itacademy.s04.t01.userapi.level2andLevel3;

import cat.itacademy.s04.t01.userapi.level2andLevel3.dto.CreateUserRequest;
import cat.itacademy.s04.t01.userapi.level2andLevel3.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {}

    @Test
    void getUsers_returnsEmptyListInitially() throws Exception{
        // Simula GET /users
        // Espera un array buit
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    void createUser_returnsUserWithId() throws Exception{
        // Simula POST /users amb JSON
        // Espera que torni el mateix usuari amb UUID no nul
        CreateUserRequest json = new CreateUserRequest("Camila Fraire", "camila@example.com");
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(json)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Camila Fraire"))
                .andExpect(jsonPath("$.email").value("camila@example.com"));

    }

    @Test
    void getUserById_returnsCorrectUser() throws Exception{
        // Primer afegeix un usuari amb POST
        // Després GET /users/{id} i comprova que torni aquest usuari
        CreateUserRequest json = new CreateUserRequest("Raquel", "raquel@example.com");

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(json)))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User createdUser = objectMapper.readValue(response, User.class);
        mockMvc.perform(get("/users/" + createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Raquel"))
                .andExpect(jsonPath("$.email").value("raquel@example.com"));

    }

    @Test
    void getUserById_returnsNotFoundIfMissing() throws Exception {
        // Simula GET /users/{id} amb un id aleatori
        // Espera 404
        mockMvc.perform(get("/users/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());

    }

    @Test
    void getUsers_withNameParam_returnsFilteredUsers() throws Exception{
        // Afegeix dos usuaris amb POST
        // Fa GET /users?name=jo i comprova que només torni els que contenen "jo"
        CreateUserRequest jsonAda = new CreateUserRequest("Ada Lovelace", "ada@example.com");
        CreateUserRequest jsonJorge = new CreateUserRequest("Jorge Navarro", "jorge@example.com");


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jsonAda)));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jsonJorge)));

        mockMvc.perform(get("/users?name=jo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Jorge Navarro"));

    }
}
