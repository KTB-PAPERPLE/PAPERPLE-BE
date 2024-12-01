package com.ktb.paperplebe.search.controller;

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

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb.paperplebe.auth.config.jwt.RefreshTokenRepository;
import com.ktb.paperplebe.auth.service.TokenService;
import com.ktb.paperplebe.paper.dto.PaperResponse;
import com.ktb.paperplebe.search.dto.SearchRequest;
import com.ktb.paperplebe.search.dto.SearchResponse;
import com.ktb.paperplebe.search.service.SearchService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SearchControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private SearchService searchService;
  @MockBean private TokenService tokenService;
  @MockBean private RefreshTokenRepository refreshTokenRepository;


  @Test
  @DisplayName("검색 요청 - 성공")
  void search() throws Exception {
    // given
    String keyword = "Sample";
    int page = 0;
    int size = 20;

    List<PaperResponse> papers =
        List.of(
            new PaperResponse(
                1L,
                "Sample Content 1",
                "http://example.com/1",
                List.of("tag1", "tag2"),
                "Sample Summary 1",
                "http://example.com/image1.jpg",
                LocalDateTime.now(),
                "nickname1",
                "profile1.jpg"),
            new PaperResponse(
                2L,
                "Sample Content 2",
                "http://example.com/2",
                List.of("tag3", "tag4"),
                "Sample Summary 2",
                "http://example.com/image2.jpg",
                LocalDateTime.now(),
                "nickname2",
                "profile2.jpg"));

    SearchResponse searchResponse = SearchResponse.of(papers);

    when(searchService.search(any(SearchRequest.class), any(PageRequest.class)))
        .thenReturn(searchResponse);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            get("/search")
                .param("keyword", keyword)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("orderBy", "id")
                .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.papers[0].content").value("Sample Content 1"))
        .andExpect(jsonPath("$.papers[1].content").value("Sample Content 2"));

    // restdocs
    resultActions.andDo(
        document(
            "검색 요청",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(
                ResourceSnippetParameters.builder()
                    .tag("Search API")
                    .summary("검색 요청")
                    .queryParameters(
                        parameterWithName("keyword").description("검색 키워드"),
                        parameterWithName("page").description("페이지 번호"),
                        parameterWithName("size").description("페이지 크기"),
                        parameterWithName("orderBy").optional().description("정렬 기준"))
                    .responseFields(
                        fieldWithPath("papers[].paperId").type("NUMBER").description("페이퍼 ID"),
                        fieldWithPath("papers[].content").type("STRING").description("페이퍼 내용"),
                        fieldWithPath("papers[].tags").type("ARRAY").description("태그 목록"),
                        fieldWithPath("papers[].newspaperLink").type("STRING").description("뉴스 링크"),
                        fieldWithPath("papers[].newspaperSummary")
                            .type("STRING")
                            .description("뉴스 요약"),
                        fieldWithPath("papers[].image").type("STRING").description("이미지 링크"),
                        fieldWithPath("papers[].createdAt").type("STRING").description("생성 시간"),
                        fieldWithPath("papers[].nickname").type("STRING").description("작성자 닉네임"),
                        fieldWithPath("papers[].profileImage")
                            .type("STRING")
                            .description("작성자 프로필 이미지"))
                    .build())));
  }
}
