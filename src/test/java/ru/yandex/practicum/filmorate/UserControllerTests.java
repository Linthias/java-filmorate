package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    private Gson gson;
    private MvcResult response;
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void initialization() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new GsonLocalDateAdapter())
                .create();
        user = new User("smth@yandex.ru", "login", "Ivan", LocalDate.of(1995, 6, 18));
        user.setId(2);
    }

    @Test
    public void positiveTest() throws Exception {
        user.setId(3);
        String body = gson.toJson(user);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals(body, response.getResponse().getContentAsString(),
                "wrong response");
    }

    @Test
    public void emptyUserTest() throws Exception {
        String body = "";

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        assertEquals("", response.getResponse().getContentAsString(),
                "wrong response");
    }

    @Test
    public void wrongIdTest() throws Exception {
        user.setId(-24);
        String body = gson.toJson(user);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void emptyEmailTest() throws Exception {
        user.setEmail("");
        String body = gson.toJson(user);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void wrongEmailTest() throws Exception {
        user.setEmail("this-is-email");
        String body = gson.toJson(user);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void emptyLoginTest() throws Exception {
        user.setLogin("");
        String body = gson.toJson(user);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void wrongLoginTest() throws Exception {
        user.setLogin("login ");
        String body = gson.toJson(user);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void emptyNameTest() throws Exception {
        user.setName("");
        String body = gson.toJson(user);
        user.setName(user.getLogin());

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals(gson.toJson(user), response.getResponse().getContentAsString(),
                "wrong response");
    }

    @Test
    public void birthdayInFutureTest() throws Exception {
        user.setBirthday(LocalDate.of(2025, 12, 1));
        String body = gson.toJson(user);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }
}
