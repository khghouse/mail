package com.mail.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LeaveMailRequest {

    @Email
    private String email;

    public LeaveMailServiceRequest toServiceRequest() {
        return LeaveMailServiceRequest.of(email);
    }

}
