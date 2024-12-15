package com.ktb.paperplebe.paper.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.ktb.paperplebe.user.constant.UserRole.ROLE_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb.paperplebe.auth.config.jwt.JwtUtil;
import com.ktb.paperplebe.auth.config.refreshtoken.RefreshTokenRepository;
import com.ktb.paperplebe.auth.service.TokenService;
import com.ktb.paperplebe.paper.dto.PaperRequest;
import com.ktb.paperplebe.paper.dto.PaperResponse;
import com.ktb.paperplebe.paper.service.PaperService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaperControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private JwtUtil jwtUtil;

  @MockBean private PaperService paperService;
  @MockBean private TokenService tokenService;
  @MockBean private RefreshTokenRepository refreshTokenRepository;

  @Test
  @WithMockUser
  @DisplayName("페이퍼 생성 - 성공")
  void createPaper() throws Exception {
    // given
    Long userId = 1L;
    PaperRequest paperRequest =
        new PaperRequest(
            "Sample Content",
            "http://example.com",
            List.of("tag1", "tag2"),
            "Sample Summary",
            "http://example.com/image.jpg");

    String paperJson = objectMapper.writeValueAsString(paperRequest);

    PaperResponse paperResponse =
        new PaperResponse(
            1L,
            "Sample Content",
            "http://example.com",
            List.of("tag1", "tag2"),
            "Sample Summary",
            "http://example.com/image.jpg",
            LocalDateTime.now(),
            "nickname",
            "profile.jpg");

    when(paperService.createPaper(any(PaperRequest.class), eq(userId))).thenReturn(paperResponse);

    String accessToken = jwtUtil.generateAccessToken(1L, ROLE_USER);
    MockCookie accessCookie = new MockCookie("AccessToken", accessToken);

    String refreshToken = jwtUtil.generateRefreshToken(1L);
    MockCookie refreshCookie = new MockCookie("RefreshToken", refreshToken);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            post("/paper")
                .cookie(refreshCookie)
                .cookie(accessCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paperJson));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("paperId").value(1L))
        .andExpect(jsonPath("content").value("Sample Content"));

    // restdocs
    resultActions.andDo(
        document(
            "페이퍼 생성",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(
                ResourceSnippetParameters.builder()
                    .tag("Paper API")
                    .summary("페이퍼 생성")
                    .requestFields(
                        fieldWithPath("content").type("STRING").description("페이퍼 내용"),
                        fieldWithPath("newspaperLink").type("STRING").description("뉴스 링크"),
                        fieldWithPath("tags").type("ARRAY").description("태그 목록"),
                        fieldWithPath("newspaperSummary").type("STRING").description("뉴스 요약"),
                        fieldWithPath("image").type("STRING").description("이미지 링크"))
                    .responseFields(
                        fieldWithPath("paperId").type("NUMBER").description("페이퍼 ID"),
                        fieldWithPath("content").type("STRING").description("페이퍼 내용"),
                        fieldWithPath("tags").type("ARRAY").description("태그 목록"),
                        fieldWithPath("newspaperLink").type("STRING").description("뉴스 링크"),
                        fieldWithPath("newspaperSummary").type("STRING").description("뉴스 요약"),
                        fieldWithPath("image").type("STRING").description("이미지 링크"),
                        fieldWithPath("createdAt").type("STRING").description("생성 시간"),
                        fieldWithPath("nickname").type("STRING").description("작성자 닉네임"),
                        fieldWithPath("profileImage").type("STRING").description("작성자 프로필 이미지"))
                    .build())));
  }

  @Test
  @WithMockUser
  @DisplayName("페이퍼 조회 - 성공")
  void getPaper() throws Exception {
    Long paperId = 1L;
    PaperResponse paperResponse =
        new PaperResponse(
            paperId,
            "Sample Content",
            "http://example.com",
            List.of("tag1", "tag2"),
            "Sample Summary",
            "http://example.com/image.jpg",
            LocalDateTime.now(),
            "nickname",
            "profile.jpg");

    when(paperService.getPaper(paperId)).thenReturn(paperResponse);

    mockMvc
        .perform(get("/paper/{paperId}", paperId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("paperId").value(paperId))
        .andExpect(jsonPath("content").value("Sample Content"))
        .andDo(
            document(
                "페이퍼 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("Paper API")
                        .summary("페이퍼 조회")
                        .pathParameters(parameterWithName("paperId").description("조회할 페이퍼 ID"))
                        .responseFields(
                            fieldWithPath("paperId").type("NUMBER").description("페이퍼 ID"),
                            fieldWithPath("content").type("STRING").description("페이퍼 내용"),
                            fieldWithPath("tags").type("ARRAY").description("태그 목록"),
                            fieldWithPath("newspaperLink").type("STRING").description("뉴스 링크"),
                            fieldWithPath("newspaperSummary").type("STRING").description("뉴스 요약"),
                            fieldWithPath("image").type("STRING").description("이미지 링크"),
                            fieldWithPath("createdAt").type("STRING").description("생성 시간"),
                            fieldWithPath("nickname").type("STRING").description("작성자 닉네임"),
                            fieldWithPath("profileImage").type("STRING").description("작성자 프로필 이미지"))
                        .build())));
  }

  @Test
  @WithMockUser
  @DisplayName("페이퍼 목록 조회 - 성공")
  void getPaperList() throws Exception {
    List<PaperResponse> papers =
        List.of(
            new PaperResponse(
                1L,
                "Content 1",
                "http://example1.com",
                List.of("tag1"),
                "Summary 1",
                "http://image1.com",
                LocalDateTime.now(),
                "nickname1",
                "profile1.jpg"),
            new PaperResponse(
                2L,
                "Content 2",
                "http://example2.com",
                List.of("tag2"),
                "Summary 2",
                "http://image2.com",
                LocalDateTime.now(),
                "nickname2",
                "profile2.jpg"));

    when(paperService.getPaperList(any(), eq("createdAt"))).thenReturn(papers);

    mockMvc
        .perform(get("/paper").param("page", "0").param("size", "20").param("orderBy", "createdAt"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andDo(
            document(
                "페이퍼 목록 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("Paper API")
                        .summary("페이퍼 목록 조회")
                        .queryParameters(
                            parameterWithName("page").description("페이지 번호"),
                            parameterWithName("size").description("페이지 크기"),
                            parameterWithName("orderBy").description("정렬 기준"))
                        .responseFields(
                            fieldWithPath("[].paperId").type("NUMBER").description("페이퍼 ID"),
                            fieldWithPath("[].content").type("STRING").description("페이퍼 내용"),
                            fieldWithPath("[].tags").type("ARRAY").description("태그 목록"),
                            fieldWithPath("[].newspaperLink").type("STRING").description("뉴스 링크"),
                            fieldWithPath("[].newspaperSummary")
                                .type("STRING")
                                .description("뉴스 요약"),
                            fieldWithPath("[].image").type("STRING").description("이미지 링크"),
                            fieldWithPath("[].createdAt").type("STRING").description("생성 시간"),
                            fieldWithPath("[].nickname").type("STRING").description("작성자 닉네임"),
                            fieldWithPath("[].profileImage")
                                .type("STRING")
                                .description("작성자 프로필 이미지"))
                        .build())));
  }

  @Test
  @WithMockUser
  @DisplayName("페이퍼 삭제 - 성공")
  void deletePaper() throws Exception {
    Long paperId = 1L;

    mockMvc
        .perform(delete("/paper/{paperId}", paperId))
        .andExpect(status().isOk())
        .andDo(
            document(
                "페이퍼 삭제",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("Paper API")
                        .summary("페이퍼 삭제")
                        .pathParameters(parameterWithName("paperId").description("삭제할 페이퍼 ID"))
                        .responseFields(
                            fieldWithPath("message").type("STRING").description("삭제 성공 메시지"))
                        .build())));
  }
}
