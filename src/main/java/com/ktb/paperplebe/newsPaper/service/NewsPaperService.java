package com.ktb.paperplebe.newsPaper.service;

import com.ktb.paperplebe.newsPaper.dto.NewsPaperResponse;
import com.ktb.paperplebe.newsPaper.entity.NewsPaper;
import com.ktb.paperplebe.newsPaper.repository.NewsPaperRepository;
import com.ktb.paperplebe.paper.dto.PaperResponse;
import com.ktb.paperplebe.paper.entity.Paper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewsPaperService {

    private final NewsPaperRepository newsPaperRepository;

    public NewsPaper findById(Long id) {
        return newsPaperRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("NewsPaper with ID " + id + " not found"));
    }

    public List<NewsPaperResponse> getNewsPaperList(Pageable pageable) {
        Page<NewsPaper> newsPaperPage = newsPaperRepository.findAll(pageable);

        return newsPaperPage.stream()
                .map(NewsPaperResponse::of) // 각 NewsPaper 객체에 대해 NewsPaperResponse.of 호출
                .collect(Collectors.toList());
    }
}
