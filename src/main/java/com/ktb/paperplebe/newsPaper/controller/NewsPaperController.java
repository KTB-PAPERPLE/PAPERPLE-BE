package com.ktb.paperplebe.newsPaper.controller;

import com.ktb.paperplebe.newsPaper.dto.NewsPaperResponse;
import com.ktb.paperplebe.newsPaper.entity.NewsPaper;
import com.ktb.paperplebe.newsPaper.service.NewsPaperService;
import com.ktb.paperplebe.paper.dto.PaperResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/news-paper")
public class NewsPaperController {

    private final NewsPaperService newsPaperService;

    @GetMapping("/{id}")
    public ResponseEntity<NewsPaper> getNewsPaperById(@PathVariable Long id) {
        NewsPaper newsPaper = newsPaperService.findById(id);
        return ResponseEntity.ok(newsPaper);
    }

    @GetMapping
    public ResponseEntity<?> getNewsPaperList(@RequestParam(defaultValue = "0") final int page,
                                              @RequestParam(defaultValue = "20") final int size) {
        final Pageable pageable = PageRequest.of(page, size);
        final List<NewsPaperResponse> newsPaperListResponse = newsPaperService.getNewsPaperList(pageable);
        return ResponseEntity.ok(newsPaperListResponse);
    }
}
