package com.rvs.tbot.bot;

import com.rvs.tbot.model.User;
import com.rvs.tbot.service.AnekdotovNetClient;
import com.rvs.tbot.service.CourseClient;
import org.apache.commons.validator.EmailValidator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

public class Utils {
    private static final CourseClient courseClient = new CourseClient();
    private static final AnekdotovNetClient anekdotovNetClient = new AnekdotovNetClient();

    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static String processUpdating(User user, String text) {
        String lowerText = text.toLowerCase();
        if (lowerText.startsWith("help") ||
                lowerText.startsWith("?")) {
            StringBuilder msg = new StringBuilder()
                    .append("список комманд:\r\n")

                    .append("?, help: Перечень доступных комманд.\r\n")

                    .append("now, time, date, время, дата, сейчас: ")
                    .append("Текущие дата и время по МСК.\r\n")

                    .append("$, usd, dollar, доллар: ")
                    .append("Текущий курс доллара MOEX и ЦБ.\r\n")

                    .append("E, eur, euro, евро: ")
                    .append("Текущий курс евро MOEX и ЦБ.\r\n")

                    .append("нефть: ")
                    .append("Текущие котировки нефти Brent, MOEX.\r\n")

                    .append("an, anekdot, анекдот: ")
                    .append("Рассказать случайный анекдот.\r\n");
            return msg.toString();
        }

        if (lowerText.startsWith("$") ||
                lowerText.startsWith("usd") ||
                lowerText.startsWith("dollar") ||
                lowerText.startsWith("доллар")) {
            return "Курс доллара: " + courseClient.getCourse("$");
        }

        if (text.equals("E") ||
                lowerText.startsWith("eur") ||
                lowerText.startsWith("евро")) {
            return "Курс евро: " + courseClient.getCourse("E");
        }

        if (lowerText.startsWith("нефть") ) {
            return "Стоимость барреля нефти Brent, $: " + courseClient.getCourse("N");
        }

        if (lowerText.startsWith("now") ||
                lowerText.startsWith("time") ||
                lowerText.startsWith("date") ||
                lowerText.startsWith("время") ||
                lowerText.startsWith("дата") ||
                lowerText.startsWith("сейчас")) {
            return "MSK+03 " + LocalDateTime.now();
        }

        if (text.equals("an") ||
                lowerText.startsWith("anekdot") ||
                lowerText.startsWith("анекдот")) {
            return anekdotovNetClient.getAnekdot();
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
