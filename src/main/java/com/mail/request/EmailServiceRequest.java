package com.mail.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailServiceRequest {

    private String to;
    private String subject;
    private String templateName;
    private Map<String, Object> variables;

    public static EmailServiceRequest of(String to, String subject, String templateName, Map<String, Object> variables) {
        EmailServiceRequest request = new EmailServiceRequest();
        request.to = to;
        request.subject = subject;
        request.templateName = templateName;
        request.variables = variables;
        return request;
    }

}
