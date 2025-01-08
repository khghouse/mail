package com.mail.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinMailRequest {

    private String to;
    private String id;

    public static JoinMailRequest of(String to, String id) {
        JoinMailRequest request = new JoinMailRequest();
        request.to = to;
        request.id = id;
        return request;
    }

}
