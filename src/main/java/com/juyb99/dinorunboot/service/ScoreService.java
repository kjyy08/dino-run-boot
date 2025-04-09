package com.juyb99.dinorunboot.service;

import com.juyb99.dinorunboot.dto.request.ScoreRequestDTO;
import com.juyb99.dinorunboot.dto.response.ScoreResponseDTO;
import com.juyb99.dinorunboot.model.Score;
import com.juyb99.dinorunboot.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final ScoreRepository scoreRepository;

    public List<ScoreResponseDTO> findAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "point"));
        return scoreRepository.findAll(pageRequest).stream()
                .map((score) -> new ScoreResponseDTO(score.getScoreId(), score.getNickname(), score.getPoint()))
                .toList();
    }

    public ScoreResponseDTO findById(long id) {
        Score score = scoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Score not found"));
        return new ScoreResponseDTO(score.getScoreId(), score.getNickname(), score.getPoint());
    }

    @Transactional
    public ScoreResponseDTO saveScore(@RequestBody ScoreRequestDTO scoreRequestDTO) {
        Score score = new Score(scoreRequestDTO.nickname(), scoreRequestDTO.point());
        score = scoreRepository.save(score);
        return new ScoreResponseDTO(score.getScoreId(), score.getNickname(), score.getPoint());
    }
}
