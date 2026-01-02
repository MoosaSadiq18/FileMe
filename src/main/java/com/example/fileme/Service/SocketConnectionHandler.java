package com.example.fileme.Service;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocketConnectionHandler extends TextWebSocketHandler {
    List<WebSocketSession> webSocketSessionsList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        super.afterConnectionEstablished(session);
        System.out.println("Connection established "+ session.getId());
        webSocketSessionsList.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        super.afterConnectionClosed(session,status);
        System.out.println("Connection close "+ session.getId());
        webSocketSessionsList.remove(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception{
        super.handleMessage(session,message);

        for(WebSocketSession i: webSocketSessionsList) {
            if (session == i) {
                continue;
            } else {
                i.sendMessage(message);
            }
        }
    }
}

