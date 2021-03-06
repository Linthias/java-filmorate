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
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTests {
    private Gson gson;
    private MvcResult response;
    private Film film;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void initialization() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new GsonLocalDateAdapter())
                .create();
        film = new Film("Star Wars. Episode I: The Phantom Menace", "desc1",
                LocalDate.of(1999, 5, 19), 133);
        film.setId(2);
    }

    @Test
    public void positiveTest() throws Exception {
        String body = gson.toJson(film);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals(body, response.getResponse().getContentAsString(),
                "wrong response");
    }

    @Test
    public void emptyFilmTest() throws Exception {
        String body = "";

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        assertEquals("", response.getResponse().getContentAsString(),
                "wrong response");
    }

    @Test
    public void wrongIdTest() throws Exception {
        film.setId(-24);
        String body = gson.toJson(film);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void emptyNameTest() throws Exception {
         film.setName("");
         String body = gson.toJson(film);

         response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(body))
                 .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                 .andReturn();
    }

    @Test
    public void tooLongDescriptionTest() throws Exception {
        film.setDescription("???? ??????... ?? ?????????????? ?????? ???? ?????????????????? ?????????????????????? ?????????? ?????? ???????????????? (?????? ???? " +
                "???????????????????? ?????????? ?????????? ?? ?????????? ?????????????????????? ????????). ?? ???????????? ???????????????? ?? ???????????? ????????????????, " +
                "?????????????????????????? 9 ???????????????? 1513 ???? ??????????-?????????????????????? ??????????????, ?????????? ?????????????? ?? ???????????????????? ??? ???? ?? ???????????? " +
                "?????????????????????? ????????. ?????????? ?????? ?????????????? ???????? ?????????? ?????????? ???? ?????????????????? ?? ?????????????? ??????????????????, " +
                "???????????? ?????????????????????? ?? ?????????????? ???? ?????????????? ?????????????? ????????????. ???????? ?????????????? ????????????.\n" +

                "???????????? ?????????? ?? ????????, ?????? ???????????????? ???????? ????-???????????????????? ??????????????????????: ???? ???? ???????? ?????????? ???????????????? " +
                "?????????? ?????????????? ?????????????? ???????????????? ??? ?? ?????? ???????? ???????????????? ??????????????. ???????? ???????? ???????????? ???????????? ?????????????????? " +
                "?????????????????????????? ?? ???????????? ???????????????????? ?????????? 26 ?????????? ??????????????. ?????????????? ??????, ?????? ???????????????????????? ?? ???????? " +
                "?? ?????????? ?????????????? VIII ???? ???????????? ?????????????? ?????????????????? ?????????????? 30 ?????????? ?????????????? (?????????? ?????? " +
                "???????? ???????????? ???? ???????? ??????????????????: ?????????????? ??????????????????).\n" +

                "?????????? ??????????????, ???????????? ?????????????????????? ???????????? ???????? IV ?????????? ???????????? ??? ?? ???????????????? ?????????? ????????????, " +
                "?????????? ?????? ???????????? ???????????????????????? ?? ?????????????? ?? ????????????, ?? ?????????????? VIII ???? ???????????? ???? ???????????????? ????. " +
                "???????? ?????? ???????????? ?????????? ???? ?????????????? ???????????????? ?????????? ???????????????????? ???????? ???????? ???? ???????????? ??????????????. " +
                "???????????????? ???? ?????????????? ?????????? ???????? ???? ?????????????????? ??? ?? ?????? ???????????????????????? ?????????????? ?? ???????? " +
                "???????? ?????????? ?????????????????????? ????????.\n" +

                "?????? ?????????????? ?????????? ???????????? ????????????????, ?????????????????? ?????? ???????????? ????????????, ?????? ?????? ???????? ???????? ??????????????????.");
        String body = gson.toJson(film);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void wrongReleaseDateTest() throws Exception {
        film.setReleaseDate(LocalDate.of(1703, 5, 27));
        String body = gson.toJson(film);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void negativeDurationTest() throws Exception {
        film.setDuration(-133);
        String body = gson.toJson(film);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }
}
