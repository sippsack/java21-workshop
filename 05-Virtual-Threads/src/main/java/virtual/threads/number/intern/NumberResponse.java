
package virtual.threads.number.intern;

import virtual.threads.number.NumberFunction;
import virtual.threads.server.protocol.Response;

import java.io.Serializable;

public record NumberResponse(
	String server,
	double value,
	NumberFunction op,
	double result
) implements Serializable, Response {

	public NumberFunction getOperation() {
		return op;
	}

	public double getValue() {
		return value;
	}

	public double getResult() {
		return result;
	}

	@Override
	public String toString() {
		return op + "(" + value + ") = " + result + "  (" + server + ")";
	}

}
