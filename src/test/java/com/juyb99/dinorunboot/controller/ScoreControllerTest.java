package com.juyb99.dinorunboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juyb99.dinorunboot.controller.api.ScoreController;
import com.juyb99.dinorunboot.dto.request.ScoreRequestDTO;
import com.juyb99.dinorunboot.dto.response.ScoreResponseDTO;
import com.juyb99.dinorunboot.service.ScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScoreController.class)
@Import(ScoreControllerTest.TestConfig.class)
public class ScoreControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public ScoreService scoreService() {
            return Mockito.mock(ScoreService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScoreService scoreService;

    @Test
    @DisplayName("모든 점수 조회 API 테스트")
    @WithMockUser
    void findAllTest() throws Exception {
        // Given
        List<ScoreResponseDTO> scores = Arrays.asList(
                new ScoreResponseDTO(1L, "player1", 300),
                new ScoreResponseDTO(2L, "player2", 200),
                new ScoreResponseDTO(3L, "player3", 100)
        );
        when(scoreService.findAll(anyInt(), anyInt())).thenReturn(scores);

        // When & Then
        mockMvc.perform(get("/api/scores")
                        .with(csrf())
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].scoreId", is(1)))
                .andExpect(jsonPath("$[0].nickname", is("player1")))
                .andExpect(jsonPath("$[0].point", is(300)))
                .andExpect(jsonPath("$[1].scoreId", is(2)))
                .andExpect(jsonPath("$[1].nickname", is("player2")))
                .andExpect(jsonPath("$[1].point", is(200)))
                .andExpect(jsonPath("$[2].scoreId", is(3)))
                .andExpect(jsonPath("$[2].nickname", is("player3")))
                .andExpect(jsonPath("$[2].point", is(100)));
    }

    @Test
    @DisplayName("모든 점수 조회 API 예외 테스트")
    @WithMockUser
    void findAllExceptionTest() throws Exception {
        // Given
        when(scoreService.findAll(anyInt(), anyInt())).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/scores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", is("Database error")));
    }

    @Test
    @DisplayName("ID로 점수 조회 API 테스트")
    @WithMockUser
    void findOneScoreTest() throws Exception {
        // Given
        ScoreResponseDTO score = new ScoreResponseDTO(1L, "player1", 300);
        when(scoreService.findById(1L)).thenReturn(score);

        // When & Then
        mockMvc.perform(get("/api/scores/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreId", is(1)))
                .andExpect(jsonPath("$.nickname", is("player1")))
                .andExpect(jsonPath("$.point", is(300)));
    }

    @Test
    @DisplayName("ID로 점수 조회 API 예외 테스트")
    @WithMockUser
    void findOneScoreExceptionTest() throws Exception {
        // Given
        when(scoreService.findById(anyLong())).thenThrow(new RuntimeException("Score not found"));

        // When & Then
        mockMvc.perform(get("/api/scores/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", is("Score not found")));
    }

    @Test
    @DisplayName("점수 저장 API 테스트")
    @WithMockUser
    void saveScoreTest() throws Exception {
        // Given
        ScoreRequestDTO requestDTO = new ScoreRequestDTO("newPlayer", 500);
        ScoreResponseDTO responseDTO = new ScoreResponseDTO(4L, "newPlayer", 500);
        when(scoreService.saveScore(any(ScoreRequestDTO.class))).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/scores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Score saved")));
    }

    @Test
    @DisplayName("점수 저장 API 예외 테스트")
    @WithMockUser
    void saveScoreExceptionTest() throws Exception {
        // Given
        ScoreRequestDTO requestDTO = new ScoreRequestDTO("newPlayer", 500);
        when(scoreService.saveScore(any(ScoreRequestDTO.class))).thenThrow(new RuntimeException("Could not save score"));

        // When & Then
        mockMvc.perform(post("/api/scores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", is("Could not save score")));
    }

    @Test
    @DisplayName("점수 저장 API null 반환 테스트")
    @WithMockUser
    void saveScoreNullResponseTest() throws Exception {
        // Given
        ScoreRequestDTO requestDTO = new ScoreRequestDTO("newPlayer", 500);
        when(scoreService.saveScore(any(ScoreRequestDTO.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(post("/api/scores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", is("Could not save score")));
    }
}
