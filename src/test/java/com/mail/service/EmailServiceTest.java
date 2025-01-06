package com.mail.service;

import com.mail.request.EmailServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static com.mail.service.EmailService.MAIL_TEMPLATE_DIRECTORY;
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
        String templateName = MAIL_TEMPLATE_DIRECTORY + "welcome";
        Map<String, Object> variables = Map.of("id", "khghouse");

        EmailServiceRequest request = EmailServiceRequest.of(to, subject, templateName, variables);

        // when
        emailService.sendEmail(request);
    }

    @Test
    @DisplayName("유효하지 않은 이메일 주소라면 예외가 발생한다.")
    void sendEmailInvalidTo() {
        // given
        String to = "khghouse@naver..com";
        String subject = "메일 발송 기능 테스트";
        String templateName = MAIL_TEMPLATE_DIRECTORY + "welcome";
        Map<String, Object> variables = Map.of("id", "khghouse");

        EmailServiceRequest request = EmailServiceRequest.of(to, subject, templateName, variables);

        // when, then
        assertThatThrownBy(() -> emailService.sendEmail(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("메일 발송에 실패했습니다.");
    }

    @Test
    @DisplayName("메일 템플릿 안, 변수가 바인딩 되지 않더라도 디폴트 값을 출력한다.")
    void sendEmailNotVariables() {
        // given
        String to = "khghouse@naver.com";
        String subject = "메일 발송 기능 테스트";
        String templateName = MAIL_TEMPLATE_DIRECTORY + "welcome";

        EmailServiceRequest request = EmailServiceRequest.of(to, subject, templateName, null);

        // when
        emailService.sendEmail(request);
    }

}
