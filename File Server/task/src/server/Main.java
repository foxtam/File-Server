package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final String host = "127.0.0.1";
    private static final int port = 34567;

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
