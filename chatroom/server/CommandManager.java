package chatroom.server;

import chatroom.server.command.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandManager{
    private static final String DELIMITER = "\\|";
    private final Map<String, Command> commands;
    private final Command defaultCommand = new DefaultCommand();

    public CommandManager(SessionManager sessionManager) {
        commands = new HashMap<>();
        commands.put("/join", new JoinCommand(sessionManager));
        commands.put("/message", new MessageCommand(sessionManager));
        commands.put("/users", new UsersCommand(sessionManager));
        commands.put("/change", new ChangeCommand(sessionManager));
        commands.put("/exit", new ExitCommand());
    }

    public void execute(String totalMessage, Session session) throws IOException {
        String[] args = totalMessage.split(DELIMITER);
        String key = args[0];

        Command command = commands.getOrDefault(key, defaultCommand);
        command.execute(args, session);
    }
}
