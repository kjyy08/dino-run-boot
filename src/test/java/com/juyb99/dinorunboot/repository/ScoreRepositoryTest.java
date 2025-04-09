package com.juyb99.dinorunboot.repository;

import com.juyb99.dinorunboot.model.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ScoreRepositoryTest {
    @Autowired
    private ScoreRepository scoreRepository;

    @Test
    @DisplayName("점수 생성 테스트")
    void createScore() {
        // given
        Score score = new Score("tester01", 1000);
        scoreRepository.save(score);

        // when
        Optional<Score> result = scoreRepository.findByNickname("tester01");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getNickname()).isEqualTo("tester01");
        assertThat(result.get().getPoint()).isEqualTo(1000);
    }

    @Test
    @DisplayName("전체 점수 조회 테스트")
    void findAll() {
        List<Score> result = scoreRepository.findAll();
        int sizeBefore = result.size();

        for (int i = 0; i < 3; i++) {
            Score score = new Score("tester%s".formatted(i), i);
            scoreRepository.save(score);
        }

        result = scoreRepository.findAll();
        int sizeAfter = result.size();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(sizeAfter - sizeBefore);
    }
}
