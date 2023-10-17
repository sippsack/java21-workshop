package virtual.threads.number;

import virtual.threads.server.protocol.ErrorResponse;
import virtual.threads.server.protocol.Response;

public class NumberException extends Exception {

    private final NumberSession session;

    public NumberException(ErrorResponse response, NumberSession session) {
        super(response.reason() + ": " + response.message());
        this.session = session;
    }

    public NumberException(Response response, NumberSession session) {
        super(response.getClass().getName());
        this.session = session;
    }

    public NumberException(String message, NumberSession session) {
        super(message);
        this.session = session;
    }

    public NumberSession getSession() {
        return session;
    }

}
