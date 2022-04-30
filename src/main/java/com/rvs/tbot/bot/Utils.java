package com.rvs.tbot.bot;

import com.rvs.tbot.model.User;
import org.apache.commons.validator.EmailValidator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

public class Utils {
    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static String processUpdating(User user, String text) {
        if (text.toLowerCase().startsWith("help")) {
            StringBuilder msg = new StringBuilder()
                    .append("список комманд:\r\n")
                    .append("now, time, date, время, дата, сейчас: ")
                    .append("Текущие дата и время по МСК.");
        }

        if (text.toLowerCase().startsWith("now") ||
                text.startsWith("time") ||
                text.startsWith("date") ||
                text.startsWith("время") ||
                text.startsWith("дата") ||
                text.startsWith("сейчас")) {
            return "MSK+03 " + LocalDateTime.now();
        }

        StringBuilder msg = new StringBuilder()
                .append("Your name: ")
                .append(user.getFirstName())
                .append("\r\n")
                .append("Your username: ")
                .append(user.getUserName())
                .append("\r\n")
                .append("You write to me: ")
                .append(text);
        return msg.toString();
    }

    public static void processSending(BotContext context, String text) {
        SendMessage message = new SendMessage();
        message.setChatId (context.getUser().getChatId());
        message.setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
