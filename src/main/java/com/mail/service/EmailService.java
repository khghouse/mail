package com.mail.service;

import com.mail.request.EmailServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;

    public void sendEmail(EmailServiceRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getText());
        message.setFrom(from);

        try {
            javaMailSender.send(message);
        } catch (MailAuthenticationException e) { // SMTP 서버 인증 실패
            // 관리자한테 알림
        } catch (MailSendException e) { // 네트워크 이슈
            // 재발송 로직
        } catch (MailException e) {
            
        }
    }

}
