package com.mail.service;

import com.mail.IntegrationTestSupport;
import com.mail.request.JoinMailServiceRequest;
import com.mail.request.LeaveMailServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MailServiceTest extends IntegrationTestSupport {

    @Autowired
    private MailService mailService;

    @Test
    @DisplayName("메일을 정상 발송한다.")
    void sendMail() {
        // given
        String email = "khghouse@naver.com";
        String id = "khghouse";

        JoinMailServiceRequest request = JoinMailServiceRequest.of(email, id);

        // when
        mailService.sendJoinMail(request);
    }

    @Test
    @DisplayName("유효하지 않은 이메일 주소라면 예외가 발생한다.")
    void sendMailInvalidEmail() {
        // given
        String email = "khghouse@naver..com";
        String id = "khghouse";

        JoinMailServiceRequest request = JoinMailServiceRequest.of(email, id);

        // when, then
        assertThatThrownBy(() -> mailService.sendJoinMail(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("메일 발송에 실패했습니다.");
    }

    @Test
    @DisplayName("메일 템플릿 안, 변수가 바인딩 되지 않더라도 디폴트 값을 출력한다.")
    void sendMailNotVariables() {
        // given
        String email = "khghouse@naver.com";

        JoinMailServiceRequest request = JoinMailServiceRequest.of(email, null);

        // when
        mailService.sendJoinMail(request);
    }

    @Test
    @DisplayName("메일을 정상 발송한다.")
    void sendLeaveMail() {
        // given
        String email = "khghouse@naver.com";

        LeaveMailServiceRequest request = LeaveMailServiceRequest.of(email);

        // when
        mailService.sendLeaveMail(request);
    }

}
