package com.ktb.paperplebe.paper.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb.paperplebe.auth.config.refreshtoken.RefreshTokenRepository;
import com.ktb.paperplebe.auth.service.TokenService;
import com.ktb.paperplebe.paper.service.PaperLikeFacade;
import com.ktb.paperplebe.paper.service.PaperLikeService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaperLikeControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private PaperLikeFacade paperLikeFacade;
  @MockBean private PaperLikeService paperLikeService;
  @MockBean private TokenService tokenService;
  @MockBean private RefreshTokenRepository refreshTokenRepository;

  @Test
  @WithMockUser
  @DisplayName("좋아요 추가 - 성공")
  void increaseLikeCount() throws Exception {
    Long paperId = 1L;
    Long userId = 1L;

    doNothing().when(paperLikeFacade).increaseLikeCount(eq(userId), eq(paperId));

    mockMvc
        .perform(post("/paper/{paperId}/likes", paperId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("successful").value(true))
        .andDo(
            document(
                "좋아요 추가",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("Paper Like API")
                        .summary("좋아요 추가")
                        .pathParameters(parameterWithName("paperId").description("좋아요를 추가할 페이퍼 ID"))
                        .responseFields(
                            fieldWithPath("statusCode").type("NUMBER").description("HTTP 상태 코드"),
                            fieldWithPath("successful").type("BOOLEAN").description("요청 성공 여부"))
                        .build())));
  }

  @Test
  @WithMockUser
  @DisplayName("좋아요 삭제 - 성공")
  void decreaseLikeCount() throws Exception {
    Long paperId = 1L;
    Long userId = 1L;

    doNothing().when(paperLikeFacade).decreaseLikeCount(userId, paperId);

    mockMvc
        .perform(delete("/paper/{paperId}/likes", paperId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("successful").value(true))
        .andDo(
            document(
                "좋아요 삭제",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("Paper Like API")
                        .summary("좋아요 삭제")
                        .pathParameters(parameterWithName("paperId").description("좋아요를 삭제할 페이퍼 ID"))
                        .responseFields(
                            fieldWithPath("statusCode").type("NUMBER").description("HTTP 상태 코드"),
                            fieldWithPath("successful").type("BOOLEAN").description("요청 성공 여부"))
                        .build())));
  }

  @Test
  @WithMockUser
  @DisplayName("좋아요 상태 조회 - 성공")
  void getLikeStatus() throws Exception {
    Long userId = 1L;
    List<Long> paperIds = List.of(1L, 2L, 3L);
    Map<Long, Boolean> likeStatus = Map.of(1L, true, 2L, false, 3L, true);

    when(paperLikeService.getLikeStatus(eq(userId), eq(paperIds))).thenReturn(likeStatus);

    mockMvc
        .perform(get("/paper/likes").param("paperIds", "1,2,3"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "좋아요 상태 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("Paper Like API")
                        .summary("좋아요 상태 조회")
                        .queryParameters(
                            parameterWithName("paperIds").description("좋아요 상태를 조회할 페이퍼 ID 목록"))
                        .build())));
  }
}
