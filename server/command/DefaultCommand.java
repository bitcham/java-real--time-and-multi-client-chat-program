package chatroom.server.command;

import chatroom.server.Session;

import java.io.IOException;
import java.util.Arrays;

public class DefaultCommand implements Command {
    @Override
    public void execute(String[] args, Session session) throws IOException {
        session.send("Unknown command: " + Arrays.toString(args));
    }
}
