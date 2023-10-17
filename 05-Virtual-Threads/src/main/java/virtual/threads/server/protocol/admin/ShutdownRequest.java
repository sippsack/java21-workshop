
package virtual.threads.server.protocol.admin;

import java.io.Serializable;

public record ShutdownRequest(long token) implements Serializable {}
