
package virtual.threads.server.protocol.user;

import java.io.Serializable;

import virtual.threads.server.protocol.Response;

public record LogoutResponse(
    String server
) implements Serializable, Response {}
