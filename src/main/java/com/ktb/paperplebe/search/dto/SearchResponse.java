package com.ktb.paperplebe.search.dto;

import com.ktb.paperplebe.paper.dto.PaperResponse;

import java.util.List;

public record SearchResponse(List<PaperResponse> papers /*, List<NewsResponse> news */) {

    public static SearchResponse of(List<PaperResponse> papers) {
        return new SearchResponse(papers);
    }
}
