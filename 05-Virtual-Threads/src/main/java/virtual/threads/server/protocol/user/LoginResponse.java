
package virtual.threads.server.protocol.user;

import java.io.Serializable;

import virtual.threads.server.protocol.Response;
import virtual.threads.server.protocol.SessionInfo;

public record LoginResponse(    
    String server,
    SessionInfo session
) implements Serializable, Response {
    
}
