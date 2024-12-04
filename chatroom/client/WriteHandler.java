package chatroom.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static util.MyLogger.log;

public class WriteHandler implements Runnable {

    private static final String DELIMITER = "|";

    private DataOutputStream output;
    private final Client client;

    private boolean closed = false;

    public WriteHandler(DataOutputStream output, Client client) {
        this.output = output;
        this.client = client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try{
            String username = inputUsername(scanner);
            output.writeUTF("/join" + DELIMITER + username);

            while(true){
                String toSend = scanner.nextLine(); // blocking
                if(toSend.isEmpty()) continue;

                if(toSend.equals("/exit")){
                    output.writeUTF(toSend);
                    break;
                }

                // If starts with "/", it's a command, otherwise it's a regular message
                if(toSend.startsWith("/")){
                    output.writeUTF(toSend);
                } else{
                    output.writeUTF("/message" + DELIMITER + toSend);
                }
            }

        } catch (IOException | NoSuchElementException e) {
            log(e);
        } finally {
            client.close();
        }
    }

    private static String inputUsername(Scanner scanner) {
        System.out.println("Please enter your name:");
        String username;
        do{
            username = scanner.nextLine();
        } while(username.isEmpty());
        return username;
    }

    public synchronized void close() {
        if(closed) return;
        try{
            System.in.close(); // Close the Scanner
        } catch (IOException e) {
            log(e);
        }
        closed = true;
        log("writeHandler closed");
    }
}
