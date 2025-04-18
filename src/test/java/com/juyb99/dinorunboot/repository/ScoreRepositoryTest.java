package com.juyb99.dinorunboot.repository;

import com.juyb99.dinorunboot.model.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ScoreRepositoryTest {

    @Autowired
    private ScoreRepository scoreRepository;

    @Test
    @DisplayName("점수 저장 테스트")
    void saveScoreTest() {
        // Given
        String nickname = "testPlayer";
        Integer point = 100;
        Score score = new Score(nickname, point);

        // When
        Score savedScore = scoreRepository.save(score);

        // Then
        assertThat(savedScore).isNotNull();
        assertThat(savedScore.getScoreId()).isGreaterThan(0);
        assertThat(savedScore.getNickname()).isEqualTo(nickname);
        assertThat(savedScore.getPoint()).isEqualTo(point);
        assertThat(savedScore.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("닉네임으로 점수 조회 테스트")
    void findByNicknameTest() {
        // Given
        String nickname = "testPlayer";
        Integer point = 100;
        Score score = new Score(nickname, point);
        scoreRepository.save(score);

        // When
        Optional<Score> foundScore = scoreRepository.findByNickname(nickname);

        // Then
        assertThat(foundScore).isPresent();
        assertThat(foundScore.get().getNickname()).isEqualTo(nickname);
        assertThat(foundScore.get().getPoint()).isEqualTo(point);
    }

    @Test
    @DisplayName("존재하지 않는 닉네임으로 점수 조회 테스트")
    void findByNonExistentNicknameTest() {
        // Given
        String nonExistentNickname = "nonExistentPlayer";

        // When
        Optional<Score> foundScore = scoreRepository.findByNickname(nonExistentNickname);

        // Then
        assertThat(foundScore).isEmpty();
    }

    @Test
    @DisplayName("페이지네이션 및 정렬 테스트")
    void findAllWithPaginationAndSortingTest() {
        // Given
        scoreRepository.save(new Score("player1", 100));
        scoreRepository.save(new Score("player2", 200));
        scoreRepository.save(new Score("player3", 150));
        scoreRepository.save(new Score("player4", 300));
        scoreRepository.save(new Score("player5", 250));

        // When - 점수 내림차순으로 정렬하여 상위 3개 조회
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "point"));
        Page<Score> scorePage = scoreRepository.findAll(pageRequest);
        List<Score> scores = scorePage.getContent();

        // Then
        assertThat(scores).hasSize(3);
        assertThat(scores.get(0).getPoint()).isEqualTo(300); // 1등
        assertThat(scores.get(1).getPoint()).isEqualTo(250); // 2등
        assertThat(scores.get(2).getPoint()).isEqualTo(200); // 3등
    }
}