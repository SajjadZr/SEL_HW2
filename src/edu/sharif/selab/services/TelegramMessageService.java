package edu.sharif.selab.services;

import edu.sharif.selab.models.EmailMessage;
import edu.sharif.selab.models.SmsMessage;
import edu.sharif.selab.models.TelegramMessage;

import java.util.regex.Pattern;

public class TelegramMessageService implements MessageService {

    /**
     * A new method to handle sending Telegram messages.
     * It validates the source and target identifiers before sending.
     * @param telegramMessage The Telegram message object to be sent.
     */
    public void sendTelegramMessage(TelegramMessage telegramMessage) {
        if (validateIdentifier(telegramMessage.getSourceId()) && validateIdentifier(telegramMessage.getTargetId())) {
            System.out.println("Sending a Telegram message from " + telegramMessage.getSourceId() + " to " + telegramMessage.getTargetId() + " with content : " + telegramMessage.getContent());
        } else {
            throw new IllegalArgumentException("Telegram ID or Phone Number is not correct!");
        }
    }

    /**
     * Validates if the given identifier is either a valid Telegram ID or a valid phone number.
     * @param identifier The identifier string to validate.
     * @return true if the identifier is valid, false otherwise.
     */
    private boolean validateIdentifier(String identifier) {
        return isTelegramId(identifier) || isPhoneNumber(identifier);
    }

    /**
     * Checks if the identifier is a valid Telegram ID.
     * A valid ID starts with '@', is at least 5 characters long, and contains letters, numbers, or underscores.
     * @param identifier The identifier to check.
     * @return true if it's a valid Telegram ID.
     */
    private boolean isTelegramId(String identifier) {
        if (identifier == null || identifier.length() < 5) {
            return false;
        }
        // Regex for Telegram ID: starts with @, followed by 5-32 alphanumeric characters and underscores.
        String telegramIdRegex = "^@[a-zA-Z0-9_]{5,}$";
        return Pattern.matches(telegramIdRegex, identifier);
    }

    /**
     * Checks if the identifier is a valid phone number (11 digits).
     * @param phoneNumber The string to check.
     * @return true if it's a valid phone number.
     */
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

    // These methods are implemented because the MessageService interface requires them.
    // In this "without-solid" step, they have empty bodies as they are not relevant to this service.
    @Override
    public void sendSmsMessage(SmsMessage smsMessage) {
        // Empty Body
    }

    @Override
    public void sendEmailMessage(EmailMessage emailMessage) {
        // Empty Body
    }
}