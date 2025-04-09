package com.juyb99.dinorunboot.controller.api;


import com.juyb99.dinorunboot.dto.request.ScoreRequestDTO;
import com.juyb99.dinorunboot.dto.response.ScoreResponseDTO;
import com.juyb99.dinorunboot.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "20") int pageSize) {
        List<ScoreResponseDTO> scores;
        try {
            scores = scoreService.findAll(pageNumber, pageSize);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/{scoreId}")
    public ResponseEntity<?> findOneScore(@PathVariable(name = "scoreId") long id) {
        ScoreResponseDTO score;

        try {
            score = scoreService.findById(id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body(score);
    }

    @PostMapping
    public ResponseEntity<?> saveScore(@RequestBody ScoreRequestDTO score) {
        try {
            ScoreResponseDTO scoreResponseDTO = scoreService.saveScore(score);
            if (scoreResponseDTO == null) {
                throw new Exception("Could not save score");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok("Score saved");
    }
}
