package com.ktb.paperplebe.newspaper.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ktb.paperplebe.auth.config.jwt.RefreshTokenRepository;
import com.ktb.paperplebe.auth.service.TokenService;
import com.ktb.paperplebe.newsPaper.dto.NewsPaperResponse;
import com.ktb.paperplebe.newsPaper.entity.NewsPaper;
import com.ktb.paperplebe.newsPaper.service.NewsPaperService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NewsPaperControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private NewsPaperService newsPaperService;
  @MockBean private TokenService tokenService;
  @MockBean private RefreshTokenRepository refreshTokenRepository;

  @Test
  @DisplayName("신문 상세 조회 - 성공")
  void getNewsPaperById() throws Exception {
    // given
    Long newsPaperId = 1L;
    NewsPaper newsPaper =
        NewsPaper.builder()
            .title("Sample Title")
            .body("Sample Body")
            .summary("Sample Summary")
            .link("http://example.com")
            .linkHash("sampleHash")
            .image("http://image.com")
            .source("Sample Source")
            .publishedAt(LocalDateTime.of(2023, 12, 25, 15, 0))
            .createdAt(LocalDateTime.of(2023, 12, 24, 10, 0))
            .build();

    when(newsPaperService.findById(newsPaperId)).thenReturn(newsPaper);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            get("/news-paper/{id}", newsPaperId).contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Sample Title"))
        .andExpect(jsonPath("$.summary").value("Sample Summary"))
        .andExpect(jsonPath("$.link").value("http://example.com"))
        .andExpect(jsonPath("$.image").value("http://image.com"))
        .andExpect(jsonPath("$.source").value("Sample Source"))
        .andExpect(jsonPath("$.publishedAt").value("2023-12-25T15:00:00"))
        .andDo(
            document(
                "신문 상세 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("News Paper API")
                        .summary("신문 상세 조회")
                        .pathParameters(parameterWithName("id").description("조회할 신문 ID"))
                        .responseFields(
                            fieldWithPath("id").type("NUMBER").description("신문 ID"),
                            fieldWithPath("title").type("STRING").description("신문 제목"),
                            fieldWithPath("body").type("STRING").description("신문 본문"),
                            fieldWithPath("summary").type("STRING").description("신문 요약"),
                            fieldWithPath("link").type("STRING").description("신문 링크"),
                            fieldWithPath("linkHash").type("STRING").description("신문 링크 해시값"),
                            fieldWithPath("image").type("STRING").description("이미지 URL"),
                            fieldWithPath("source").type("STRING").description("신문 출처"),
                            fieldWithPath("publishedAt").type("STRING").description("발행 시간"),
                            fieldWithPath("createdAt").type("STRING").description("생성 시간"),
                            fieldWithPath("updatedAt").type("STRING").description("생성 시간"),
                            fieldWithPath("deletedAt").type("STRING").description("생성 시간"))
                        .build())));
  }

  @Test
  @DisplayName("신문 목록 조회 - 성공")
  void getNewsPaperList() throws Exception {
    // given
    List<NewsPaperResponse> newsPapers =
        List.of(
            new NewsPaperResponse(
                1L,
                "Title 1",
                "http://example.com/1",
                "http://image.com/1",
                "Summary 1",
                LocalDateTime.of(2023, 12, 25, 15, 0),
                LocalDateTime.of(2023, 12, 24, 10, 0)),
            new NewsPaperResponse(
                2L,
                "Title 2",
                "http://example.com/2",
                "http://image.com/2",
                "Summary 2",
                LocalDateTime.of(2023, 12, 26, 15, 0),
                LocalDateTime.of(2023, 12, 25, 10, 0)));

    when(newsPaperService.getNewsPaperList(any())).thenReturn(newsPapers);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            get("/news-paper")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andDo(
            document(
                "신문 목록 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("News Paper API")
                        .summary("신문 목록 조회")
                        .queryParameters(
                            parameterWithName("page").description("페이지 번호"),
                            parameterWithName("size").description("페이지 크기"))
                        .responseFields(
                            fieldWithPath("[].newsPaperId").type("NUMBER").description("신문 ID"),
                            fieldWithPath("[].title").type("STRING").description("신문 제목"),
                            fieldWithPath("[].link").type("STRING").description("신문 링크"),
                            fieldWithPath("[].image").type("STRING").description("이미지 URL"),
                            fieldWithPath("[].summary").type("STRING").description("신문 요약"),
                            fieldWithPath("[].publishedAt").type("STRING").description("발행 시간"),
                            fieldWithPath("[].createdAt").type("STRING").description("생성 시간"))
                        .build())));
  }
}
