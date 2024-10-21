package com.ktb.paperplebe.search.controller;

import com.ktb.paperplebe.paper.dto.PaperRequest;
import com.ktb.paperplebe.paper.dto.PaperResponse;
import com.ktb.paperplebe.paper.service.PaperService;
import com.ktb.paperplebe.search.dto.SearchRequest;
import com.ktb.paperplebe.search.dto.SearchResponse;
import com.ktb.paperplebe.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/search")
@RestController
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(@RequestParam final String keyword,
                                    @RequestParam(defaultValue = "0") final int page,
                                    @RequestParam(defaultValue = "20") final int size,
                                    @RequestParam(defaultValue = "id") final String orderBy) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        final SearchRequest searchRequest = new SearchRequest(keyword);
        final SearchResponse searchResponse = searchService.search(searchRequest, pageable);

        return ResponseEntity.ok(searchResponse);
    }
}
