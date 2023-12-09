package com.circular.Circular.economy.controller;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
@Component
public class ConnectedUserRegistry {

    private Set<String> connectedUsers = new HashSet<>();

    public void addUser(String username) {
        connectedUsers.add(username);
    }

    public void removeUser(String username) {
        connectedUsers.remove(username);
    }

    public Set<String> getConnectedUsers() {
        return new HashSet<>(connectedUsers);
    }
}