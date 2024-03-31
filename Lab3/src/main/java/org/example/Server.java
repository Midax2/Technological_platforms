package main.java.org.example;

// Server.java
import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12346)) {
            logger.info("Server started. Waiting for connections...");

            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Client connected: " + socket);

                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server exception: " + e.getMessage(), e);
        }
    }
}

class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            logger.info("Sending ready signal to client...");
            out.writeObject("ready");

            while (true) {
                Message message = (Message) in.readObject();
                logger.info("Received message from client: " + message.getContent());

                // Process the message if needed

                // Sending confirmation message back to client
                out.writeObject("Message received: " + message.getContent());
            }

        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Exception in client handler: " + e.getMessage(), e);
        }
    }
}