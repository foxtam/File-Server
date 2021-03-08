package server.request;

import java.io.File;

public class DelRequest extends Request {
    public DelRequest(String fileName) {
        super(new File(fileName));
    }

    @Override
    public String perform() {
        return null;
    }
}
