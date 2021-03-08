package server.request;

import java.io.File;

public class GetRequest extends Request {

    public GetRequest(String fileName) {
        super(new File(fileName));
    }

    @Override
    public String perform() {
        return null;
    }
}
