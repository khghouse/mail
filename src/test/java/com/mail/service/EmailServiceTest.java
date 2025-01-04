package com.mail.service;

import com.mail.request.EmailServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("메일을 정상 발송한다.")
    void sendEmail() {
        // given
        String to = "khghouse@naver.com";
        String subject = "메일 발송 기능 테스트";
        String text = "메일 내용이 잘 전달됐나요?";

        EmailServiceRequest request = EmailServiceRequest.of(to, subject, text);

        // when
        emailService.sendEmail(request);
    }

    @Test
    @DisplayName("유효하지 않은 이메일 주소라면 예외가 발생한다.")
    void sendEmailInvalidID() {
        // given
        String to = "khghouse@naver..com";
        String subject = "메일 발송 기능 테스트";
        String text = "메일 내용이 잘 전달됐나요?";

        EmailServiceRequest request = EmailServiceRequest.of(to, subject, text);

        // when, then
        assertThatThrownBy(() -> emailService.sendEmail(request))
                .isInstanceOf(MailException.class);
    }

}
