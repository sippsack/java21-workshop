package virtual.threads.weather;

import virtual.threads.server.protocol.ErrorResponse;
import virtual.threads.server.protocol.Response;

public class WeatherException extends Exception {

    private final WeatherSession session;

    public WeatherException(ErrorResponse response, WeatherSession session) {
        super(response.reason() + ": " + response.message());
        this.session = session;
    }

    public WeatherException(Response response, WeatherSession session) {
        super(response.getClass().getName());
        this.session = session;
    }

    public WeatherException(String message, WeatherSession session) {
        super(message);
        this.session = session;
    }

    public WeatherSession getSession() {
        return session;
    }

}
