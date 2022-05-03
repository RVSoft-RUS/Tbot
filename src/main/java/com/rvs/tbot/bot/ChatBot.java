package com.rvs.tbot.bot;

import com.rvs.tbot.model.User;
import com.rvs.tbot.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class);
    private static final String BROADCAST = "broadcast";
    private static final String LIST_USERS = "users";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final UserService userService;

    public ChatBot(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        
        final String text = update.getMessage().getText();
        final String chatId = String.valueOf(update.getMessage().getChatId());

        User user = userService.findByChatId(chatId);
        
        if (checkIfAdminCommand(user, text)) {
            return;
        }

        boolean newUser = false;

        if (user == null) {
            newUser = true;
            user = new User(chatId);
            user.setFirstName(update.getMessage().getFrom().getFirstName());
            user.setLastName(update.getMessage().getFrom().getLastName());
            user.setUserName(update.getMessage().getFrom().getUserName());
            if (user.getUserName().equals("NoRepositoryBean")) {
                user.setAdmin(true);
            }
            userService.addUser(user);
            LOGGER.info("New user registered: " + chatId + " " + user.getFirstName() + ". Text: " + text);
        } else {
            LOGGER.info("Update received for user: " + user.getFirstName() + ". Text: " + text);
            if (!user.getUserName().equals(update.getMessage().getFrom().getUserName())) {
                String oldName = user.getUserName();
                String newName = update.getMessage().getFrom().getUserName();
                user.setUserName(newName);
                userService.updateUser(user);
                LOGGER.info("Update user name from: " + oldName + " to " + newName);
            }
        }

        BotContext context = BotContext.of(this, user, text);

        String msg = Utils.processUpdating(user, text);
        Utils.processSending(context, msg);
        if (newUser) {
            msg = Utils.processUpdating(user, "help");
            Utils.processSending(context, msg);
        }
    }

    private boolean checkIfAdminCommand(User user, String text) {
        if (user == null || !user.getAdmin()) {
            return false;
        }

        if (text.startsWith(BROADCAST)) {
            LOGGER.info("Admin command received: " + text);
            text = text.substring(BROADCAST.length());
            broadcast(text);
            return true;
        } else if (text.equals(LIST_USERS)) {
            LOGGER.info("Admin command received: " + text);
            listUsers(user);
            return true;
        }

        return false;
    }

    private void broadcast(String text) {
        List<User> users = userService.findAllUsers();
        users.forEach(user -> sendMessage(user.getChatId(), text));
    }

    private void listUsers(User admin) {
        StringBuilder sb = new StringBuilder("All users list:\r\n");
        List<User> users = userService.findAllUsers();

        users.forEach(user ->
                sb.append(user.getId())
                        .append("   ")
                        .append(user.getFirstName())
                        .append("   ")
                        .append(user.getLastName())
                        .append("   ")
                        .append(user.getUserName())
                        .append("\r\n")
        );

        sendMessage(admin.getChatId(), sb.toString());
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId (chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
