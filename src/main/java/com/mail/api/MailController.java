package com.mail.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mail.exception.UnauthorizedException;
import com.mail.request.JoinMailRequest;
import com.mail.request.LeaveMailRequest;
import com.mail.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HEADER_PREFIX = "HMAC-SHA256 Signature=";

    @Value("${spring.mail.secret}")
    private String secret;

    private final ObjectMapper objectMapper;
    private final HttpServletRequest httpServletRequest;

    private final MailService mailService;

    @PostMapping("/join")
    public ResponseEntity<?> sendJoinMail(@RequestBody @Validated JoinMailRequest request) throws Exception {
        validationHmacSignature(objectMapper.writeValueAsString(request));
        mailService.sendJoinMail(request.toServiceRequest());
        return ResponseEntity.ok(null);
    }

    @PostMapping("/leave")
    public ResponseEntity<?> sendLeaveMail(@RequestBody @Validated LeaveMailRequest request) throws Exception {
        validationHmacSignature(objectMapper.writeValueAsString(request));
        mailService.sendLeaveMail(request.toServiceRequest());
        return ResponseEntity.ok(null);
    }

    private void validationHmacSignature(String data) throws Exception {
        String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(HEADER_PREFIX)) {
            throw new UnauthorizedException();
        }

        String clientSignature = authorization.substring(HEADER_PREFIX.length());
        String serverSignature = generateHmac(data);

        if (!serverSignature.equals(clientSignature)) {
            throw new UnauthorizedException();
        }
    }

    private String generateHmac(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(secretKey);

        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

}
