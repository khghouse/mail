package com.mail.api;

import com.mail.ControllerTestSupport;
import com.mail.request.JoinMailRequest;
import com.mail.request.LeaveMailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MailControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하고 정상 응답한다.")
    void sendJoinMail() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .id("khghouse")
                .build();

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하지만 id 값이 없으면 에러를 응답한다.")
    void sendJoinMailNotExistId() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .build();

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하지만 id 값이 공백이면 에러를 응답한다.")
    void sendJoinMailBlankId() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .id("")
                .build();

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendJoinInvalidEmail() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@")
                .id("khghouse")
                .build();

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendJoinInvalidEmail2() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("@naver.com")
                .id("khghouse")
                .build();

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendJoinInvalidEmail3() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@.com")
                .id("khghouse")
                .build();

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendJoinInvalidEmail4() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@@naver.com")
                .id("khghouse")
                .build();

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 탈퇴 안내 메일 발송을 요청하고 정상 응답한다.")
    void sendLeaveMail() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("khghouse@naver.com")
                .build();

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 탈퇴 안내 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendLeaveInvalidEmail() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("khghouse@")
                .build();

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 탈퇴 안내 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendLeaveInvalidEmail2() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("@naver.com")
                .build();

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 탈퇴 안내 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendLeaveInvalidEmail3() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("khghouse@.com")
                .build();

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 탈퇴 안내 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendLeaveInvalidEmail4() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("khghouse@@naver.com")
                .build();

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}