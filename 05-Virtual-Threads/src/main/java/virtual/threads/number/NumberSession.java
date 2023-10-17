package virtual.threads.number;

import virtual.threads.number.intern.NumberRequest;
import virtual.threads.number.intern.NumberResponse;
import virtual.threads.server.ServerId;
import virtual.threads.server.Service;
import virtual.threads.server.Session;
import virtual.threads.server.protocol.ErrorResponse;
import virtual.threads.server.protocol.SessionInfo;
import virtual.threads.weather.Weather;
import virtual.threads.weather.WeatherException;

import java.io.IOException;

public record NumberSession(
    ServerId serverId,
    SessionInfo session
) implements Session {

    public Number requestCalculation(
        NumberFunction function,
        double value
    ) throws IOException, NumberException {
        try {
            var response = Service.request(
                serverId,
                new NumberRequest(session, function, value)
            );
            switch (response) {
            case ErrorResponse e:
                throw new NumberException(e, this);
            case NumberResponse n:
                return n.doubleValue();
            default:
                throw new NumberException(response, this);
            }
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }

}
