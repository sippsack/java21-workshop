
package virtual.threads.server;

public record ServerId(int id, int delay) {

    public ServerId(int i) {
        this(i, 0);
    }
    
}
