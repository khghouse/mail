package com.mail.service;

import com.mail.request.JoinMailRequest;
import com.mail.request.LeaveMailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Test
    @DisplayName("메일을 정상 발송한다.")
    void sendMail() {
        // given
        String to = "khghouse@naver.com";
        String id = "khghouse";

        JoinMailRequest request = JoinMailRequest.of(to, id);

        // when
        mailService.sendJoinMail(request);
    }

    @Test
    @DisplayName("유효하지 않은 이메일 주소라면 예외가 발생한다.")
    void sendMailInvalidTo() {
        // given
        String to = "khghouse@naver..com";
        String id = "khghouse";

        JoinMailRequest request = JoinMailRequest.of(to, id);

        // when, then
        assertThatThrownBy(() -> mailService.sendJoinMail(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("메일 발송에 실패했습니다.");
    }

    @Test
    @DisplayName("메일 템플릿 안, 변수가 바인딩 되지 않더라도 디폴트 값을 출력한다.")
    void sendMailNotVariables() {
        // given
        String to = "khghouse@naver.com";

        JoinMailRequest request = JoinMailRequest.of(to, null);

        // when
        mailService.sendJoinMail(request);
    }

    @Test
    @DisplayName("메일을 정상 발송한다.")
    void sendLeaveMail() {
        // given
        String to = "khghouse@naver.com";

        LeaveMailRequest request = LeaveMailRequest.of(to);

        // when
        mailService.sendLeaveMail(request);
    }

}
