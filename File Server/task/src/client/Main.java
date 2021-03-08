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
        try (var socket = new Socket(InetAddress.getByName(host), port);
             var input = new DataInputStream(socket.getInputStream());
             var output = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Client started!");
            communicate(input, output);
        }
    }

    private static void communicate(DataInputStream input, DataOutputStream output) throws IOException {
        output.writeUTF(sendMessage);
        System.out.println("Sent: " + sendMessage);

        String inputMsg = input.readUTF();
        System.out.println("Received: " + inputMsg);
    }
}
