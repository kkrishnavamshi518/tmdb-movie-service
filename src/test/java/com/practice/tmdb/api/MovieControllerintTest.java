package com.practice.tmdb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.tmdb.model.Movie;
import com.practice.tmdb.repo.MovieRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MovieRepo movieRepo;

    @BeforeEach
    void cleanUp() {
        movieRepo.deleteAllInBatch();
    }

    // ✅ CREATE MOVIE TEST
    @Test
    void givenMovie_whenCreateMovie_thenReturnSavedMovie() throws Exception {

        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("ssr");
        movie.setActors(List.of("ntr", "rc", "alia"));

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("$.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", hasSize(3)));
    }

    // ✅ GET MOVIE BY ID TEST
    @Test
    void givenMovieId_whenFetchMovie_thenReturnMovie() throws Exception {

        // Given
        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("ss rajamouli");
        movie.setActors(List.of("ntr", "ramcharan", "aliabhatt"));

        Movie savedMovie = movieRepo.save(movie);

        // When + Then
        mockMvc.perform(get("/movies/" + savedMovie.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("$.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", hasSize(3)));
    }

    // ✅ UPDATE MOVIE TEST
    @Test
    void givenSavedMovie_whenUpdateMovie_thenMovieUpdatedInDb() throws Exception {

        // Given
        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("ss rajamouli");
        movie.setActors(List.of("ntr", "ramcharan", "aliabhatt"));

        Movie savedMovie = movieRepo.save(movie);
        Long id = savedMovie.getId();

        // Update movie
        movie.setActors(List.of("ntr", "ramcharan", "aliabhatt", "ajaydevgan"));

        mockMvc.perform(put("/movies/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andDo(print())
                .andExpect(status().isOk());

        // Verify updated movie
        mockMvc.perform(get("/movies/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("$.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", hasSize(4)))
                .andExpect(jsonPath("$.actors[3]", is("ajaydevgan")));
    }

    // ✅ DELETE MOVIE TEST
    @Test
    void givenMovie_whenDeleteRequest_thenMovieRemovedFromDb() throws Exception {

        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("ss rajamouli");
        movie.setActors(List.of("ntr", "ramcharan", "aliabhatt"));

        Movie savedMovie = movieRepo.save(movie);
        Long id = savedMovie.getId();

        mockMvc.perform(delete("/movies/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(movieRepo.findById(id).isPresent());
    }
}
