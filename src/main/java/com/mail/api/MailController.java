package com.mail.api;

import com.mail.request.JoinMailRequest;
import com.mail.request.LeaveMailRequest;
import com.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/join")
    public ResponseEntity<?> sendJoinMail(@RequestBody @Validated JoinMailRequest request) {
        mailService.sendJoinMail(request.toServiceRequest());
        return ResponseEntity.ok(null);
    }

    @PostMapping("/leave")
    public ResponseEntity<?> sendLeaveMail(@RequestBody @Validated LeaveMailRequest request) {
        mailService.sendLeaveMail(request.toServiceRequest());
        return ResponseEntity.ok(null);
    }

}
