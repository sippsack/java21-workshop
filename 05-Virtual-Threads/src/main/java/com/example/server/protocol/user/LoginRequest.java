
package com.example.server.protocol.user;

import java.io.Serializable;

import com.example.server.protocol.Request;
import com.example.server.protocol.SessionInfo;

public record LoginRequest(
    SessionInfo session, 
    String name, 
    char[] password
) implements Serializable, Request {
    
    public LoginRequest(String name, char[] password) {
        this(SessionInfo.NO_INFO, name, password);        
    }
    
}
