
package virtual.threads.number.intern;

import virtual.threads.server.protocol.Response;

import java.io.Serializable;

public record NumberResponse(
	String server,
	double value
) implements Serializable, Response {

	//@Override
	//public String toString() {
	//	return asString() + "   (" + server + ")";
	//}

	public int intValue() {
		return (int) value;
	}

	public long longValue() {
		return (long) value;
	}

	public float floatValue() {
		return (float) value;
	}

	public double doubleValue() {
		return value;
	}
}
