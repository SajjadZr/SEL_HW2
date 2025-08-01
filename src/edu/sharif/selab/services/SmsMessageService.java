package edu.sharif.selab.services;

import edu.sharif.selab.models.SmsMessage;

public class SmsMessageService implements MessageService<SmsMessage> {
    @Override
    public void send(SmsMessage message) {
        if (validatePhoneNumber(message.getSourcePhoneNumber()) && validatePhoneNumber(message.getTargetPhoneNumber())) {
            System.out.println("Sending a SMS from " + message.getSourcePhoneNumber() + " to " + message.getTargetPhoneNumber() + " with content : " + message.getContent());
        } else {
            throw new IllegalArgumentException("Phone Number is Not Correct!");
        }
    }

    private boolean validatePhoneNumber(String phoneNumber) {
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