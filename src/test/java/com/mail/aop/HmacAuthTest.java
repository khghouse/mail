package com.mail.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mail.IntegrationTestSupport;
import com.mail.filter.RequestCachingFilter;
import com.mail.request.JoinMailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HmacAuthTest extends IntegrationTestSupport {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HEADER_PREFIX = "HMAC-SHA256 Signature=";

    @Value("${spring.mail.secret}")
    private String secret;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilter(new RequestCachingFilter())
                .build();
    }

    @Test
    @DisplayName("")
    void validationHmacSignature() throws Exception {
        // given
        JoinMailRequest request = JoinMailRequest.builder()
                .email("khghouse@naver.com")
                .id("khghouse")
                .build();

        String data = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + generateHmac(data))
                        .content(data))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String generateHmac(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(secretKey);

        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

}