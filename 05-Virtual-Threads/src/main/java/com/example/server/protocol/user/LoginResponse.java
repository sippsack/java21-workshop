
package com.example.server.protocol.user;

import java.io.Serializable;

import com.example.server.protocol.Response;
import com.example.server.protocol.SessionInfo;

public record LoginResponse(    
    String server,
    SessionInfo session
) implements Serializable, Response {
    
}
