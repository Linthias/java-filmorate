package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = FilmController.class)
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
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andReturn();
    }

    @Test
    public void emptyNameTest() throws Exception {
         film.setName("");
         String body = gson.toJson(film);

         response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(body))
                 .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                 .andReturn();
    }

    @Test
    public void tooLongDescriptionTest() throws Exception {
        film.setDescription("Ну что... В прошлый раз мы разобрали предысторию битвы при Флоддене (там же " +
                "оглавление всего цикла о Войне Камбрейской лиги). А теперь перейдём к самому сражению, " +
                "состоявшемуся 9 сентября 1513 на англо-шотландской границе, между Англией и Шотландией — но в рамках " +
                "Итальянских войн. Битве той суждено было стать одной из важнейших в истории Шотландии, " +
                "широко обсуждаемой в работах по военной истории вообще. Чему хватает причин.\n" +

                "Начать стоит с того, что сражение было по-настоящему генеральным: ну то есть прямо решающим " +
                "исход большой военной кампании — и это было очевидно заранее. Ведь граф Суррей привёл навстречу " +
                "вторгнувшимся в Англию шотландцам около 26 тысяч человек. Напомню вам, что одновременно с этим " +
                "в армии Генриха VIII на севере Франции действует порядка 30 тысяч человек (пусть там " +
                "были далеко не одни англичане: хватало наёмников).\n" +

                "Иными словами, разбей шотландский король Яков IV армию Суррея — и никакого иного выбора, " +
                "кроме как срочно возвращаться с войском в Англию, у Генриха VIII ну просто не осталось бы. " +
                "Ведь уже некому стало бы пресечь десяткам тысяч шотландцев путь хоть до самого Лондона. " +
                "Кампания во Франции тогда была бы провалена — а это кардинальный поворот в ходе " +
                "всей Войны Камбрейской лиги.\n" +

                "Обе стороны очень хорошо понимали, насколько тут высоки ставки, так что вели себя осторожно.");
        String body = gson.toJson(film);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andReturn();
    }

    @Test
    public void wrongReleaseDateTest() throws Exception {
        film.setReleaseDate(LocalDate.of(1703, 5, 27));
        String body = gson.toJson(film);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andReturn();
        assertEquals("Film has wrong release date", response.getResolvedException().getMessage(),
                "wrong exception message");
    }

    @Test
    public void negativeDurationTest() throws Exception {
        film.setDuration(-133);
        String body = gson.toJson(film);

        response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andReturn();
    }
}
