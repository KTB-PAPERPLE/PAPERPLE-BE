package com.ktb.paperplebe.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb.paperplebe.user.dto.UserInfoResponse;
import com.ktb.paperplebe.user.dto.UserNicknameRequest;
import com.ktb.paperplebe.user.dto.UserProfileImageRequest;
import com.ktb.paperplebe.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("유저 정보 조회 - 성공")
  void getUserInfo() throws Exception {
    // given
    Long userId = 1L;
    UserInfoResponse response = new UserInfoResponse(userId, "nickname", "profile.jpg");
    when(userService.getUserInfo(any())).thenReturn(response);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId))
        .andExpect(jsonPath("$.nickname").value("nickname"))
        .andExpect(jsonPath("$.profileImage").value("profile.jpg"))
        .andDo(
            document(
                "유저 정보 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("User API")
                        .summary("유저 정보 조회")
                        .responseFields(
                            fieldWithPath("userId").description("유저 ID"),
                            fieldWithPath("nickname").description("유저 닉네임"),
                            fieldWithPath("profileImage").description("유저 프로필 이미지"))
                        .build())));
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("닉네임 수정 - 성공")
  void updateNickname() throws Exception {
    // given
    Long userId = 1L;
    String newNickname = "newNickname";
    UserNicknameRequest request = new UserNicknameRequest(newNickname);
    UserInfoResponse response = new UserInfoResponse(userId, newNickname, "profile.jpg");
    when(userService.updateNickname(any(), any())).thenReturn(response);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            patch("/user/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId))
        .andExpect(jsonPath("$.nickname").value(newNickname))
        .andExpect(jsonPath("$.profileImage").value("profile.jpg"))
        .andDo(
            document(
                "닉네임 수정",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("User API")
                        .summary("닉네임 수정")
                        .requestFields(fieldWithPath("nickname").description("변경할 닉네임"))
                        .responseFields(
                            fieldWithPath("userId").description("유저 ID"),
                            fieldWithPath("nickname").description("변경된 유저 닉네임"),
                            fieldWithPath("profileImage").description("유저 프로필 이미지"))
                        .build())));
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("프로필 이미지 수정 - 성공")
  void updateProfileImage() throws Exception {
    // given
    Long userId = 1L;
    String newProfileImage = "new-profile.jpg";
    UserProfileImageRequest request = new UserProfileImageRequest(newProfileImage);
    UserInfoResponse response = new UserInfoResponse(userId, "nickname", newProfileImage);
    when(userService.updateProfileImage(any(), any())).thenReturn(response);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            patch("/user/profile-image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId))
        .andExpect(jsonPath("$.nickname").value("nickname"))
        .andExpect(jsonPath("$.profileImage").value(newProfileImage))
        .andDo(
            document(
                "프로필 이미지 수정",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("User API")
                        .summary("프로필 이미지 수정")
                        .requestFields(fieldWithPath("profileImage").description("변경할 프로필 이미지 URL"))
                        .responseFields(
                            fieldWithPath("userId").description("유저 ID"),
                            fieldWithPath("nickname").description("유저 닉네임"),
                            fieldWithPath("profileImage").description("변경된 유저 프로필 이미지"))
                        .build())));
  }
}
