package at.htl.carparkmanagement.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{user}")
@ApplicationScoped
public class SupportWebSocket {
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user) {
        sessions.put(user, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("user") String user) {
        sessions.remove(user);
    }

    @OnError
    public void onError(Session session, @PathParam("user") String user, Throwable throwable) {
        sessions.remove(user);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("user") String username) {
        if (message.equalsIgnoreCase("_ready_")) {
            broadcast("Welcome " + username + " to our Support chat, how can we help you?");
        }
        else {
            broadcast(">>" + username + ": " + message);
            broadcast("Your are contacting us outside of our business hours, please try again later");
        }
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> s.getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                System.out.println("Unable to send message; " + result.getException());
            }
        }));
    }
}
