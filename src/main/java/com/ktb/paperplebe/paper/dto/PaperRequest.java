package com.ktb.paperplebe.paper.dto;

import java.util.List;

public record PaperRequest(
        String content,
        String newspaperLink,
        List<String> tags,
        String newspaperSummary,
        String image
) {
}