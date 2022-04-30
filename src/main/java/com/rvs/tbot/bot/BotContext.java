package com.rvs.tbot.bot;

import com.rvs.tbot.model.User;

public class BotContext {
    private final ChatBot bot;
    private final User user;
    private final String input;

    public static BotContext of(ChatBot bot, User user, String input) {
        return new BotContext(bot, user, input);
    }

    public BotContext(ChatBot bot, User user, String input) {
        this.bot = bot;
        this.user = user;
        this.input = input;
    }

    public User getUser() {
        return user;
    }

    public ChatBot getBot() {
        return bot;
    }

    public String getInput() {
        return input;
    }
}
