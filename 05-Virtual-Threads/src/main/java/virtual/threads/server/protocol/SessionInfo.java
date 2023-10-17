
package virtual.threads.server.protocol;

import java.io.Serializable;

/**
 * Beschreibt eine Session.
 */
public record SessionInfo(long id, long token) implements Serializable {

    public static final SessionInfo NO_INFO = new SessionInfo(0, 0);
    
    private int bigEndianShort(long x, int i) {
    	var shift = 16 * i;
    	return ((int) (x >>> shift)) & 0xFFFF;
    }
    
    public String toString() {
    	return String.format(
    		"SessionInfo[" + 
				"id: %04d" + 
				", " +
				"token: %04X_%04X_%04X_%04X" +
			"]",
			id,
			bigEndianShort(token, 0),
			bigEndianShort(token, 1),
			bigEndianShort(token, 2),
			bigEndianShort(token, 3)				
		); 
    }

}
