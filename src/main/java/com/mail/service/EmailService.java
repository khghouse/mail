package com.mail.service;

import com.mail.request.EmailServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.*;
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
            System.err.println(e.getMessage());
            throw new RuntimeException("SMTP 서버 인증에 실패했습니다.");
            // 관리자한테 알림
        } catch (MailSendException e) { // 네트워크 이슈
            // 재발송 로직
        } catch (MailParseException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("잘못된 이메일 주소 또는 본문 파싱에 실패했습니다.");
        } catch (MailException e) {

        }
    }

}
