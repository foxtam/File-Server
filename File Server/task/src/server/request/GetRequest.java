package server.request;

import core.Response;

import java.io.IOException;
import java.nio.file.Files;

public class GetRequest extends Request {

    public GetRequest(String fileName) {
        super(fileName);
    }

    @Override
    public String perform() throws IOException {
        return Files.exists(filePath)
                ? "" + Response.OK_CODE + " " + Files.readString(filePath)
                : "" + Response.NO_FILE_CODE;
    }
}
