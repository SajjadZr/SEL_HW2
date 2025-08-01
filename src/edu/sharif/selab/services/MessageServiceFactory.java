package edu.sharif.selab.services;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MessageServiceFactory {
    private final Map<String, Supplier<MessageService<?>>> registry = new HashMap<>();

    public MessageServiceFactory() {
        registry.put("sms", SmsMessageService::new);
        registry.put("email", EmailMessageService::new);
        registry.put("telegram", TelegramMessageService::new);
    }

    public MessageService<?> getService(String type) {
        Supplier<MessageService<?>> serviceSupplier = registry.get(type.toLowerCase());
        if (serviceSupplier == null) {
            throw new IllegalArgumentException("Unknown service type: " + type);
        }
        return serviceSupplier.get();
    }
}