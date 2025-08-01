package edu.sharif.selab.services;

import edu.sharif.selab.models.Message;

public interface MessageService<T extends Message> {
    void send(T message);
}