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
    private static final String sendMessage = "All files were sent!";

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host))) {
            System.out.println("Server started!");
            Socket socket = serverSocket.accept();
            communicate(socket);
        }
    }

    private static void communicate(Socket socket) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        try (socket; dataInputStream; dataOutputStream) {
            String inputMsg = dataInputStream.readUTF();
            System.out.println("Received: " + inputMsg);

            dataOutputStream.writeUTF(sendMessage);
            System.out.println("Sent: " + sendMessage);
        }
    }
}
