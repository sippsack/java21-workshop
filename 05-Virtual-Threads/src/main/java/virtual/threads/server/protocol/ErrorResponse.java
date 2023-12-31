
package virtual.threads.server.protocol;

import java.io.Serializable;

public record ErrorResponse(
    String server, 
    Reason reason, 
    String message
) implements Serializable, Response {}
