package com.mail.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinMailRequest {

    @Email
    private String email;

    @NotBlank(message = "회원 아이디를 입력해 주세요.")
    private String id;

    public JoinMailServiceRequest toServiceRequest() {
        return JoinMailServiceRequest.of(email, id);
    }

}
