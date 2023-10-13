
package com.example.weather;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.server.protocol.Request;
import com.example.server.protocol.SessionInfo;

public record ForecastRequest(
    SessionInfo session,
    String area, 
    LocalDateTime date_time
) implements Serializable, Request {}
