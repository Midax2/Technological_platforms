package main.java.org.example;

// Client.java
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.*;
public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12346);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            logger.info("Connected to server.");

            // Reading user input
            logger.info("Enter a number:");
            int number = Integer.parseInt(scanner.nextLine());

            // Sending the number to server
            out.writeObject(number);

            // Sending messages to server
            for (int i = 0; i < number; i++) {
                Message message = new Message();
                logger.info("Enter message " + (i+1) + ":");
                message.setContent(scanner.nextLine());
                out.writeObject(message);
            }

            // Receiving responses from server
            while (true) {
                String response = (String) in.readObject();
                logger.info("Server response: " + response);

                // Break the loop if server finished sending responses
                if (response.equals("finished")) {
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Exception in client: " + e.getMessage(), e);
        }
    }
}
