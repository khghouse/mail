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
        JoinMailServiceRequest object = new JoinMailServiceRequest();
        object.email = email;
        object.id = id;
        return object;
    }

}
