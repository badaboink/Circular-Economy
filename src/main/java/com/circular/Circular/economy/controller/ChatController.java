package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.model.Message;
import com.circular.Circular.economy.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;


@CrossOrigin(origins = "http://localhost:3030")
@Controller
public class ChatController extends TextWebSocketHandler {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ConnectedUserRegistry connectedUserRegistry;
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        if ("JOIN".equals(message.getStatus().name())) {
            connectedUserRegistry.addUser(message.getSenderName());

            sendExistingUsersToNewUser(message.getSenderName());
        }
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println(message.toString());
        return message;
    }

    private Message createJoinMessage(String username) {
        Message joinMessage = new Message();
        joinMessage.setSenderName(username);
        joinMessage.setStatus(Status.JOIN);
        joinMessage.setMessage("");
        return joinMessage;
    }
    private void sendExistingUsersToNewUser(String newUserName) {
        Set<String> existingUsers = connectedUserRegistry.getConnectedUsers();
        for (String existingUser : existingUsers) {
            if (!existingUser.equals(newUserName)) {
                // Send a message to the newly joined user from each existing user
                simpMessagingTemplate.convertAndSendToUser(newUserName, "/private", createJoinMessage(existingUser));
            }
        }
    }
}
