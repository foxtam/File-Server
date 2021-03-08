package server.request;

import java.io.File;

public class PutRequest extends Request {
    private final String content;

    public PutRequest(String request) {
        super(new File(request.split("\\s+")[0]));
        this.content = request.split("\\s+")[1];
    }

    @Override
    public String perform() {
        return null;
    }
}
