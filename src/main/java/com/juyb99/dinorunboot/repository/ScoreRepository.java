package com.juyb99.dinorunboot.repository;

import com.juyb99.dinorunboot.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByNickname(String nickname);
}
