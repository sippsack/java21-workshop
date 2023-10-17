package virtual.threads.number.intern;

import virtual.threads.server.ServiceServer;
import virtual.threads.server.protocol.ErrorResponse;
import virtual.threads.server.protocol.Reason;
import virtual.threads.server.protocol.Request;
import virtual.threads.server.protocol.Response;

import java.util.concurrent.ThreadLocalRandom;

public class NumberServiceServer extends ServiceServer {

    private final ThreadLocalRandom rng;

    public NumberServiceServer(int id, int delay) {
        super(id, delay);
        rng = ThreadLocalRandom.current();
    }

    @Override
    protected Response handleRequest(Request request) {
        System.out.println("REQUEST " + request);
        return switch (request) {
            case NumberRequest r -> handleNumberRequest(r);
            default -> null;
        };
    }

    private Response handleNumberRequest(NumberRequest request) {
        var value = request.value();
        var op    = request.function();

        double result = Double.NaN;

        switch (op) {
            case SQUARE_ROOT:
                if (value < 0) {
                    return new ErrorResponse(
                        id,
                        Reason.ILLEGAL_PARAMETER,
                        "Parameter der Quadratwurzel darf nicht kleiner als Null sein"
                    );
                }
                result = Math.sqrt(value);
                break;
            case RANDOM:
                result = random(value);
                break;
            case IDENTITY:
                result = value;
                break;
        }

        return new NumberResponse(id, value, op, result);
    }

    private double random(double limit) {
        return rng.nextDouble(limit);
    }

}


