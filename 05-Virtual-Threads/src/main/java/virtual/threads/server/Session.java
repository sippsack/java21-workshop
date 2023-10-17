package virtual.threads.server;

import virtual.threads.server.protocol.SessionInfo;

import java.io.IOException;

public interface Session extends AutoCloseable {

    public abstract ServerId serverId();

    public abstract SessionInfo session();

    public default void logout() throws IOException {
        try {
            Service.logout(serverId(), session());
        } catch(ClassNotFoundException ignore) {
            ;
        }
    }

    @Override
    public default void close() throws IOException {
        logout();
    }

}
