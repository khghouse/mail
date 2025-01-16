package com.mail.api;

import com.mail.ControllerTestSupport;
import com.mail.request.JoinMailRequest;
import com.mail.request.LeaveMailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MailControllerTest extends ControllerTestSupport {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HEADER_PREFIX = "HMAC-SHA256 Signature=";

    @Value("${spring.mail.secret}")
    private String secret;

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하고 정상 응답한다.")
    void sendJoinMail() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .id("khghouse")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
                        .content(data))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 인증 값으로 회원 가입 메일 발송을 요청하면 에러를 응답한다.")
    void sendJoinMailInvalidAuthenticated() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .id("khghouse")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + "abcd")
                        .content(data))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("인증에 필요한 값이 누락됐거나 유효하지 않습니다."));
    }

    @Test
    @DisplayName("인증 값 없이 회원 가입 메일 발송을 요청하면 에러를 응답한다.")
    void sendJoinMailNotExistAuthenticated() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .id("khghouse")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("인증에 필요한 값이 누락됐거나 유효하지 않습니다."));
    }

    @Test
    @DisplayName("잘못된 인증 타입으로 회원 가입 메일 발송을 요청하면 에러를 응답한다.")
    void sendJoinMailInvalidAuthType() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .id("khghouse")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateHmac(data))
                        .content(data))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("인증에 필요한 값이 누락됐거나 유효하지 않습니다."));
    }

    @Test
    @DisplayName("회원 가입 메일 발송을 요청하지만 id 값이 없으면 에러를 응답한다.")
    void sendJoinMailNotExistId() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 인증 값으로 회원 탈퇴 안내 메일 발송을 요청하면 에러를 응답한다.")
    void sendLeaveMailInvalidAuthenticated() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("khghouse@naver.com")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + "abcd")
                        .content(data))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("인증에 필요한 값이 누락됐거나 유효하지 않습니다."));
    }

    @Test
    @DisplayName("인증 값 없이 회원 탈퇴 안내 메일 발송을 요청하면 에러를 응답한다.")
    void sendLeaveMailNotExistAuthenticated() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("khghouse@naver.com")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("인증에 필요한 값이 누락됐거나 유효하지 않습니다."));
    }

    @Test
    @DisplayName("잘못된 인증 타입으로 회원 탈퇴 안내 메일 발송을 요청하면 에러를 응답한다.")
    void sendLeaveMailMailInvalidAuthType() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("khghouse@naver.com")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateHmac(data))
                        .content(data))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("인증에 필요한 값이 누락됐거나 유효하지 않습니다."));
    }

    @Test
    @DisplayName("회원 탈퇴 안내 메일 발송을 요청하지만 이메일 값이 이메일 형식에 맞지 않다면 에러를 응답한다.")
    void sendLeaveInvalidEmail() throws Exception {
        // given
        LeaveMailRequest request = LeaveMailRequest.builder()
                .email("khghouse@")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
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

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String generateHmac(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(secretKey);

        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

}