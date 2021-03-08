package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String host = "127.0.0.1";
    private static final int port = 34567;
    public static Path storagePath = Path.of(System.getProperty("user.dir"), "src", "server", "data");

    static {
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host))) {
            System.out.println("Server started!");

            try (Socket socket = serverSocket.accept();
                 var input = new DataInputStream(socket.getInputStream());
                 var output = new DataOutputStream(socket.getOutputStream())) {

                new ServerCommunicator(input, output).run();
            }
        }
    }
}
