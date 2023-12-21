package com.cojar.whats_hot_backend.domain.base_module.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public void send(String email, String code, String type) throws MessagingException {

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String title = "", content = "";

        if (type.equals("임시 비밀번호")) {
            title = "What's Hot " + type + " 발송 메일입니다.";
            content = "아래 비밀번호로 로그인 하시고 비밀번호를 변경해주세요.<br>"
                    + code;
        }

        helper.setTo(email);
        helper.setSubject(title);
        helper.setText(content, true);

        this.javaMailSender.send(mimeMessage);
    }
}
