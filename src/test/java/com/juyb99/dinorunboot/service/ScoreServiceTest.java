package com.juyb99.dinorunboot.service;

import com.juyb99.dinorunboot.dto.request.ScoreRequestDTO;
import com.juyb99.dinorunboot.dto.response.ScoreResponseDTO;
import com.juyb99.dinorunboot.model.Score;
import com.juyb99.dinorunboot.repository.ScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoreService scoreService;

    private Score score1;
    private Score score2;
    private Score score3;
    private List<Score> scoreList;
    private Page<Score> scorePage;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        score1 = new Score("player1", 300);
        setPrivateField(score1, "scoreId", 1L);
        setPrivateField(score1, "createdAt", Timestamp.from(Instant.now()));

        score2 = new Score("player2", 200);
        setPrivateField(score2, "scoreId", 2L);
        setPrivateField(score2, "createdAt", Timestamp.from(Instant.now()));

        score3 = new Score("player3", 100);
        setPrivateField(score3, "scoreId", 3L);
        setPrivateField(score3, "createdAt", Timestamp.from(Instant.now()));

        scoreList = Arrays.asList(score1, score2, score3);
        scorePage = new PageImpl<>(scoreList);
    }

    // Reflection을 사용하여 private 필드 설정
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = Score.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("모든 점수 조회 테스트")
    void findAllTest() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "point"));
        when(scoreRepository.findAll(any(PageRequest.class))).thenReturn(scorePage);

        // When
        List<ScoreResponseDTO> result = scoreService.findAll(0, 10);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).scoreId()).isEqualTo(1L);
        assertThat(result.get(0).nickname()).isEqualTo("player1");
        assertThat(result.get(0).point()).isEqualTo(300);
        
        verify(scoreRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("ID로 점수 조회 테스트 - 성공")
    void findByIdSuccessTest() {
        // Given
        when(scoreRepository.findById(1L)).thenReturn(Optional.of(score1));

        // When
        ScoreResponseDTO result = scoreService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.scoreId()).isEqualTo(1L);
        assertThat(result.nickname()).isEqualTo("player1");
        assertThat(result.point()).isEqualTo(300);
        
        verify(scoreRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("ID로 점수 조회 테스트 - 실패")
    void findByIdFailTest() {
        // Given
        when(scoreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> scoreService.findById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Score not found");
        
        verify(scoreRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("점수 저장 테스트")
    void saveScoreTest() {
        // Given
        ScoreRequestDTO requestDTO = new ScoreRequestDTO("newPlayer", 500);
        Score newScore = new Score(requestDTO.nickname(), requestDTO.point());
        setPrivateField(newScore, "scoreId", 4L);
        
        when(scoreRepository.save(any(Score.class))).thenReturn(newScore);

        // When
        ScoreResponseDTO result = scoreService.saveScore(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.scoreId()).isEqualTo(4L);
        assertThat(result.nickname()).isEqualTo("newPlayer");
        assertThat(result.point()).isEqualTo(500);
        
        verify(scoreRepository, times(1)).save(any(Score.class));
    }
}