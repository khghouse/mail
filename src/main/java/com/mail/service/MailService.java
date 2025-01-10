package com.mail.service;

import com.mail.enumeration.MailTemplate;
import com.mail.request.JoinMailRequest;
import com.mail.request.LeaveMailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendJoinMail(JoinMailRequest joinMailRequest) {
        Map<String, Object> variables = this.createMapFromNonNullValues("id", joinMailRequest.getId());
        this.send(MailTemplate.JOIN, joinMailRequest.getTo(), variables);
    }

    public void sendLeaveMail(LeaveMailRequest leaveMailRequest) {
        this.send(MailTemplate.LEAVE, leaveMailRequest.getTo(), null);
    }

    private void send(MailTemplate mailTemplate, String to, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process(mailTemplate.getTemplate(), context);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(mailTemplate.getSubject());
            helper.setText(htmlContent, true);

            mailTemplate.getAttachments()
                    .stream()
                    .map(ClassPathResource::new)
                    .filter(Resource::exists)
                    .filter(Resource::isFile)
                    .forEach(resource -> {
                        try {
                            helper.addAttachment(resource.getFilename(), resource);
                        } catch (MessagingException e) {
                            log.error("메일에 첨부파일을 추가하지 못했습니다. 파일명 : {}", resource.getFilename());
                        }
                    });
        } catch (MessagingException e) {
            log.error("[errorMessage] : {}, [to] : {}, [from] : {}", e.getMessage(), to, from);
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

    private Map<String, Object> createMapFromNonNullValues(Object... variables) {
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < variables.length; i += 2) {
            String key = (String) variables[i];
            Object value = variables[i + 1];
            if (value != null) {
                map.put(key, value);
            }
        }

        return map;
    }

}
