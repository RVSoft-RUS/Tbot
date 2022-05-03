package com.rvs.tbot.mail;

import com.rvs.tbot.model.User;
import com.rvs.tbot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@PropertySource("classpath:telegram.properties")
public class NotificationService {
    private final UserService userService;
    private final JavaMailSender emailSender;

    public NotificationService(UserService userService, JavaMailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @Value("${bot.email.subject}")
    private String emailSubject;

    @Value("${bot.email.from}")
    private String emailFrom;

    @Value("${bot.email.to}")
    private String emailTo;

    @Scheduled(fixedRate = 10_000)
    public void sendNewApplications() {
        List<User> users = userService.findNewUsers();
        if (users.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        users.forEach(user ->
                sb.append("Phone: ")
                        .append(user.getFirstName())
                        .append("\r\n")
                        .append("EMAIL: ")
                        .append(user.getLastName())
                        .append("\r\n\r\n")
        );

        sendEmail(sb.toString());
    }

    private void sendEmail(String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(emailTo);
        mailMessage.setFrom(emailFrom);
        mailMessage.setSubject(emailSubject);
        mailMessage.setText(text);

        emailSender.send(mailMessage);
    }
}
