package virtual.threads.number.intern;

import virtual.threads.number.NumberFunction;
import virtual.threads.server.protocol.Request;
import virtual.threads.server.protocol.SessionInfo;

import java.io.Serializable;

public record NumberRequest(
    SessionInfo session,
    NumberFunction function,
    double value
) implements Serializable, Request {

}
