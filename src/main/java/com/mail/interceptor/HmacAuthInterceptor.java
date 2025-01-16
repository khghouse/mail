package com.mail.interceptor;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class HmacAuthInterceptor implements HandlerInterceptor {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HEADER_PREFIX = "HMAC-SHA256 Signature=";

    @Value("${spring.mail.secret}")
    private String secret;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(HEADER_PREFIX)) {
            throw new SecurityException("인증에 필요한 헤더 값이 누락됐거나 유효하지 않습니다.");
        }

        String clientSignature = authorization.substring(HEADER_PREFIX.length());
        String data = new String(cachedRequest.getContentAsByteArray());
        String serverSignature = generateHmac(data);

        if (!serverSignature.equals(clientSignature)) {
            throw new SecurityException("Unauthorized");
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
