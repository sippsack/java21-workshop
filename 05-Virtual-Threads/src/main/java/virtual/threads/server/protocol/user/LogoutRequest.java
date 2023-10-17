
package virtual.threads.server.protocol.user;

import java.io.Serializable;

import virtual.threads.server.protocol.Request;
import virtual.threads.server.protocol.SessionInfo;

public record LogoutRequest(
    SessionInfo session
) implements Serializable, Request {}
