package server.request;

import core.RequestConstants;

import java.io.File;

public abstract class Request {
    protected File file;

    protected Request(File file) {
        this.file = file;
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

    public abstract String perform();
}
