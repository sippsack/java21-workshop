
package virtual.threads.server.protocol.user;

import java.io.Serializable;

import virtual.threads.server.protocol.Request;
import virtual.threads.server.protocol.SessionInfo;

public record LoginRequest(
    SessionInfo session,
    String name, 
    char[] password
) implements Serializable, Request {
    
    public LoginRequest(String name, char[] password) {
        this(SessionInfo.NO_INFO, name, password);        
    }
    
}
