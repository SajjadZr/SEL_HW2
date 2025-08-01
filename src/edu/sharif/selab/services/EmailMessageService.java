package edu.sharif.selab.services;

import edu.sharif.selab.models.EmailMessage;
import java.util.regex.Pattern;

public class EmailMessageService implements MessageService<EmailMessage> {
    @Override
    public void send(EmailMessage message) {
        if (validateEmailAddress(message.getSourceEmailAddress()) && validateEmailAddress(message.getTargetEmailAddress())) {
            System.out.println("Sending an Email from " + message.getSourceEmailAddress() + " to " + message.getTargetEmailAddress() + " with content : " + message.getContent());
        } else {
            throw new IllegalArgumentException("Email Address is Not Correct!");
        }
    }

    public boolean validateEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}