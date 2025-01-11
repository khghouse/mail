package com.mail.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinMailServiceRequest {

    private String email;
    private String id;

    public static JoinMailServiceRequest of(String email, String id) {
        JoinMailServiceRequest request = new JoinMailServiceRequest();
        request.email = email;
        request.id = id;
        return request;
    }

}
