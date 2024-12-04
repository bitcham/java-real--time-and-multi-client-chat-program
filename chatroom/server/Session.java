package chatroom.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static util.MyLogger.log;
import static util.SocketCloseUtil.closeAll;

public class Session implements Runnable{

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final CommandManager  commandManager;
    private final SessionManager sessionManager;

    private boolean closed = false;
    private String username;

    public Session(Socket socket,CommandManager commandManager, SessionManager sessionManager) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.commandManager = commandManager;
        this.sessionManager = sessionManager;
        this.sessionManager.add(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Receive message from client
                String received = input.readUTF();
                log("client -> server: " + received);

                // Execute command
                commandManager.execute(received, this);
            }
        }catch(IOException e){
            log(e);
        } finally{
            sessionManager.remove(this);
            sessionManager.sendAll(username + " has left the chat.");
            close();
        }
    }

    public void send(String message) throws IOException {
        log("server -> client: " + message);
        output.writeUTF(message);
    }

    public synchronized void close() {
        if(closed){
            return;
        }
        closeAll(socket, input, output);
        closed = true;
        log("Connection closed: " + socket);  // Changed from Korean to English
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
