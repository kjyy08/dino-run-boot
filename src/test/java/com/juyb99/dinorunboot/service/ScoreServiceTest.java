package com.juyb99.dinorunboot.service;

import com.juyb99.dinorunboot.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScoreServiceTest {
    @Autowired
    private ScoreRepository scoreRepository;
}
