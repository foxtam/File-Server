package core;

public class Response {
    public static final int NO_FILE_CODE = 404;
    public static final int OK_CODE = 200;
    public static final int FILE_ALREADY_EXISTS_CODE = 403;

    private final String[] response;

    public Response(String response) {
        this.response = response.split("\\s+");
    }

    public int code() {
        return Integer.parseInt(response[0]);
    }

    public String content() {
        return response[1];
    }
}
