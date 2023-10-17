package virtual.threads.weather;

import virtual.threads.server.ServerId;
import virtual.threads.server.Service;
import virtual.threads.server.Session;
import virtual.threads.server.protocol.ErrorResponse;
import virtual.threads.server.protocol.SessionInfo;
import virtual.threads.weather.intern.WeatherRequest;
import virtual.threads.weather.intern.WeatherResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public record WeatherSession(
    ServerId serverId,
    SessionInfo session
) implements Session {

    public Weather requestWeather(String area) throws IOException, WeatherException {
        return requestWeather(area, LocalDateTime.now());
    }
    public Weather requestWeather(
        String area,
        LocalDateTime time
    ) throws IOException, WeatherException {
        try {
            var response = Service.request(
                serverId,
                new WeatherRequest(session, area, time)
            );
            switch (response) {
            case ErrorResponse e:
                throw new WeatherException(e, this);
            case WeatherResponse w:
                return w;
            default:
                throw new WeatherException(response, this);
            }
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }

}
