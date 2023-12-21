package com.example.withdogandcat.domain.chat.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatMessage implements Serializable {

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;

}
