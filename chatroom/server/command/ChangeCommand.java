package chatroom.server.command;

import chatroom.server.Session;
import chatroom.server.SessionManager;

public class ChangeCommand implements Command{
    private final SessionManager sessionManager;

    public ChangeCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args, Session session) {
        String oldUsername = session.getUsername();
        String newUsername = args[1];
        session.setUsername(newUsername);
        sessionManager.sendAll(oldUsername + " has changed their name to " + newUsername);
    }
}
