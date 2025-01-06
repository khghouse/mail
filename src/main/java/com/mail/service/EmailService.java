package com.mail.service;

import com.mail.request.EmailServiceRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    public static final String MAIL_TEMPLATE_DIRECTORY = "/mail/";

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendEmail(EmailServiceRequest request) {

        Context context = new Context();
        context.setVariables(request.getVariables());
        String htmlContent = templateEngine.process(request.getTemplateName(), context);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            log.error("[errorMessage] : {}, [to] : {}, [from] : {}", e.getMessage(), request.getTo(), from);
        }

        try {
            javaMailSender.send(message);
        } catch (MailAuthenticationException e) { // SMTP 서버 인증 실패
            log.error("Authentication failed : " + e.getMessage());
            throw new RuntimeException("SMTP 서버 인증에 실패했습니다.");
            // 관리자한테 알림
        } catch (MailException e) {
            throw new RuntimeException("메일 발송에 실패했습니다.");
        }
    }

}
