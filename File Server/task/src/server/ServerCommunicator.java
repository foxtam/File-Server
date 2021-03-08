package server;

import server.request.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerCommunicator {
    private final DataInputStream input;
    private final DataOutputStream output;

    public ServerCommunicator(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void run() throws IOException {
        while (true) {
            Request request = Request.of(input.readUTF());
        }
    }
}
