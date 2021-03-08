package server.request;

import core.RequestConstants;

import java.io.IOException;
import java.nio.file.Path;

public abstract class Request {
    protected static Path storagePath = Path.of(".").resolve("server").resolve("data");
    protected final Path filePath;

    protected Request(String fileName) {
        this.filePath = storagePath.resolve(fileName);
    }

    public static Request of(String requestString) {
        String[] split = requestString.split("\\s+", 2);
        String requestType = split[0];
        switch (requestType) {
            case RequestConstants.PUT:
                return new PutRequest(split[1]);
            case RequestConstants.GET:
                return new GetRequest(split[1]);
            case RequestConstants.DEL:
                return new DelRequest(split[1]);
            default:
                throw new IllegalStateException(requestType);
        }
    }

    public abstract String perform() throws IOException;
}
