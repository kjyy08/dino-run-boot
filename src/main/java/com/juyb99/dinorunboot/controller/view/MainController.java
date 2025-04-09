package com.juyb99.dinorunboot.controller.view;

import com.juyb99.dinorunboot.dto.response.ScoreResponseDTO;
import com.juyb99.dinorunboot.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final ScoreService scoreService;

    @GetMapping
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "0") int pageNumber,
                        @RequestParam(required = false, defaultValue = "20") int pageSize) {
        List<ScoreResponseDTO> scoreResponseDTOList = scoreService.findAll(pageNumber, pageSize);
        model.addAttribute("rankingList", scoreResponseDTOList);
        return "index";
    }
}
