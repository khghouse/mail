package com.mail.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("정상적으로 메일을 발송한다.")
    void sendEmail() {
        // given
        String to = "khghouse@naver.com";
        String subject = "메일 발송 기능 테스트";
        String text = "메일 내용이 잘 전달됐나요?";

        // when
        emailService.sendEmail(to, subject, text);
    }

}
