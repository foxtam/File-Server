package server.request;

import core.Response;

import java.io.IOException;
import java.nio.file.Files;

public class DelRequest extends Request {
    public DelRequest(String fileName) {
        super(fileName);
    }

    @Override
    public String perform() throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return "" + Response.OK_CODE;
        } else {
            return "" + Response.NO_FILE_CODE;
        }
    }
}
