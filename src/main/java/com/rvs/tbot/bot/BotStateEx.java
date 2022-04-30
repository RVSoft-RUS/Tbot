package com.rvs.tbot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public enum BotStateEx {
    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Hello!\r\n" +
                    context.getUser().getFirstName() + "\r\n" +
                    context.getUser().getUserName()
                    );
        }

        @Override
        public BotStateEx nextState() {
            return EnterPhone;
        }
    },

    EnterPhone {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Enter phone:");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setFirstName(context.getInput());
        }

        @Override
        public BotStateEx nextState() {
            return EnterEmail;
        }
    },

    EnterEmail {
        private BotStateEx next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Enter email:");
        }

        @Override
        public void handleInput(BotContext context) {
           String email = context.getInput();

           if (Utils.isValidEmailAddress(email)) {
               context.getUser().setLastName(context.getInput());
               next = Approved;
           } else {
               sendMessage(context, "Wrong Email!");
               next = EnterEmail;
           }
        }

        @Override
        public BotStateEx nextState() {
            return next;
        }
    },

    Approved(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Thank you!");
        }

        @Override
        public BotStateEx nextState() {
            return Start;
        }
    };
    
    private static BotStateEx[] states;
    private final boolean inputNeeded;
    
    BotStateEx() {
        this.inputNeeded = true;
    }

    BotStateEx(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }
    
    public static BotStateEx getInitialState() {
        return byId(0);
    }

    public static BotStateEx byId(int id) {
        if (states == null) {
            states = BotStateEx.values();
        }
        return states[id];
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage();
        message.setChatId (context.getUser().getChatId());
        message.setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean isInputNeeded() {
        return inputNeeded;
    }

    public void handleInput(BotContext context) {
        //
    }

    public abstract void enter(BotContext context);
    public abstract BotStateEx nextState();
}
