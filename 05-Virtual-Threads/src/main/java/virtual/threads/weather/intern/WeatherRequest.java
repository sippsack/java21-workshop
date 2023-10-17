
package virtual.threads.weather.intern;

import java.io.Serializable;
import java.time.LocalDateTime;

import virtual.threads.server.protocol.Request;
import virtual.threads.server.protocol.SessionInfo;

public record WeatherRequest(
    SessionInfo session,
    String area,
    LocalDateTime time
) implements Serializable, Request {

    public WeatherRequest(String area) {
        this(SessionInfo.NO_INFO, area, LocalDateTime.now());
    }

    public WeatherRequest(SessionInfo session, String area) {
        this(session, area, LocalDateTime.now());
    }

}
