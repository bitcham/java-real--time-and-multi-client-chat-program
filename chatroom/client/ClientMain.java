package chatroom.client;

import java.io.IOException;

public class ClientMain {
    public static final int PORT = 12346;

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", PORT);
        client.start();
    }
}
