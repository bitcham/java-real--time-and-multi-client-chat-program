package chatroom.server.command;

import chatroom.server.Session;
import chatroom.server.SessionManager;

public class JoinCommand implements Command{
    private final SessionManager sessionManager;

    public JoinCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args, Session session) {
        String username = args[1];
        session.setUsername(username);
        sessionManager.sendAll(username + " has joined the chat.");
    }
}
