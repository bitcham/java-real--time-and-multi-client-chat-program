package chatroom.server;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class Server {
    private final CommandManager commandManager;
    private final SessionManager sessionManager;
    private final int port;

    private ServerSocket serverSocket;

    public Server(int port, CommandManager commandManager, SessionManager sessionManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.sessionManager = sessionManager;
    }

    public void start() throws IOException {
        log("Server starting " + commandManager.getClass());
        serverSocket = new ServerSocket(port);
        log("Server socket starting - listening port: " + port);

        addShutdown();
        running();
    }

    private void running() {
        try{

            while(true) {
                Socket socket = serverSocket.accept();
                log("Socket connected: " + socket);

                Session session = new Session(socket, commandManager, sessionManager);
                Thread thread = new Thread(session);
                thread.start();
            }
        }catch(IOException e){
            log("Server socket closed: " + e);
        }
    }

    private void addShutdown() {
        ShutdownHook shutdownHook = new ShutdownHook(serverSocket, sessionManager);
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook, "shutdown"));
    }

    static class ShutdownHook implements Runnable {

        private final ServerSocket serverSocket;
        private final SessionManager sessionManager;

        public ShutdownHook(ServerSocket serverSocket, SessionManager sessionManager) {
            this.serverSocket = serverSocket;
            this.sessionManager = sessionManager;
        }


        @Override
        public void run() {
            log("Shutdown hook triggered");
            try {
                sessionManager.closeAll();
                serverSocket.close();

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error during shutdown: " + e);
            }
        }
    }

}
