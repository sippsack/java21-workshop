
package virtual.threads.number;

import virtual.threads.number.intern.NumberRequest;
import virtual.threads.number.intern.NumberResponse;
import virtual.threads.server.ServerId;
import virtual.threads.server.Service;
import virtual.threads.server.Session;
import virtual.threads.server.protocol.ErrorResponse;
import virtual.threads.server.protocol.SessionInfo;

import java.io.IOException;

public record NumberSession(
    ServerId serverId,
    SessionInfo session
) implements Session {

    public double calculateSquareRoot(double x) throws NumberException {
        return requestCalculation(NumberFunction.SQUARE_ROOT, x);
    }

    public double retrieveIdentity(double x) throws NumberException {
        return requestCalculation(NumberFunction.IDENTITY, x);
    }

    public double generateRandom(double x) throws NumberException {
        return requestCalculation(NumberFunction.RANDOM, x);
    }

    public double requestCalculation(
        NumberFunction function,
        double value
    ) throws NumberException {
        try {
            var response = Service.request(
                serverId,
                new NumberRequest(session, function, value)
            );
            switch (response) {
            case ErrorResponse e:
                throw new NumberException(e, this);
            case NumberResponse n:
                return n.getResult();
            default:
                throw new NumberException(response, this);
            }
        } catch (Exception e) {
            throw new NumberException(e.getMessage(), this);
        }
    }

}
