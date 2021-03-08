package server;

import core.RequestConstants;
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

    public boolean run() throws IOException {
        String stringRequest = input.readUTF();
        if (stringRequest.equals(RequestConstants.EXIT)) {
            return false;
        } else {
            output.writeUTF(
                    Request.of(stringRequest).perform());
            return true;
        }
    }
}
