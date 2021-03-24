package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final String host = "127.0.0.1";
    private static final int port = 34567;
    private static ServerSocket staticServerSocket;

    public static void main(String[] args) throws IOException {
        staticServerSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
        try (ServerSocket serverSocket = staticServerSocket) {
            int poolSize = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(poolSize);
            System.out.println("Server started!");

            try {
                //noinspection InfiniteLoopStatement
                while (true) {
                    Socket socket = serverSocket.accept();
                    executor.submit(() -> communicate(socket));
                }
            } catch (SocketException ignored) {
            }

            executor.shutdown();
        }
    }

    private static void communicate(Socket socket) {
        try (socket) {
            new ServerCommunicator(socket, Main::shutdownServer).run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void shutdownServer() {
        try {
            staticServerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
