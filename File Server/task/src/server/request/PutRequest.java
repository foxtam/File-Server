package server.request;

import core.Response;

import java.io.IOException;
import java.nio.file.Files;

public class PutRequest extends Request {
    private final String content;

    public PutRequest(String request) {
        super(request.split("\\s+")[0]);
        this.content = request.split("\\s+")[1];
    }

    @Override
    public String perform() throws IOException {
        if (Files.exists(filePath)) {
            return "" + Response.FILE_ALREADY_EXISTS_CODE;
        } else {
            Files.createFile(filePath);
            Files.writeString(filePath, content);
            return "" + Response.OK_CODE;
        }
    }
}
