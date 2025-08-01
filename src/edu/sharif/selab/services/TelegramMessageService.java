package edu.sharif.selab.services;

import edu.sharif.selab.models.TelegramMessage;
import java.util.regex.Pattern;

public class TelegramMessageService implements MessageService<TelegramMessage> {
    @Override
    public void send(TelegramMessage message) {
        if (validateIdentifier(message.getSourceId()) && validateIdentifier(message.getTargetId())) {
            System.out.println("Sending a Telegram message from " + message.getSourceId() + " to " + message.getTargetId() + " with content : " + message.getContent());
        } else {
            throw new IllegalArgumentException("Telegram ID or Phone Number is not correct!");
        }
    }

    private boolean validateIdentifier(String identifier) {
        return isTelegramId(identifier) || isPhoneNumber(identifier);
    }

    private boolean isTelegramId(String identifier) {
        if (identifier == null) return false;
        String telegramIdRegex = "^@[a-zA-Z0-9_]{5,}$";
        return Pattern.matches(telegramIdRegex, identifier);
    }

    private boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return false;
        }
        for (char c : phoneNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}