
package com.example.weather;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.server.protocol.Request;
import com.example.server.protocol.SessionInfo;

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
