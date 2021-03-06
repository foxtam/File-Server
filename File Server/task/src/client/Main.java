package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    private static final String host = "127.0.0.1";
    private static final int port = 34567;
    private static final String sendMessage = "Give me everything you have!";

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(InetAddress.getByName(host), port)) {
            System.out.println("Client started!");
            communicate(socket);
        }
    }

    private static void communicate(Socket socket) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        try (socket; dataInputStream; dataOutputStream) {
            dataOutputStream.writeUTF(sendMessage);
            System.out.println("Sent: " + sendMessage);

            String inputMsg = dataInputStream.readUTF();
            System.out.println("Received: " + inputMsg);
        }
    }
}
