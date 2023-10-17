package virtual.threads.server;

import virtual.threads.server.protocol.SessionInfo;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Verwaltet die einzelnen Sessions und kann etwa Auskunft darüber geben,
 * ob ein Request zulässig ist oder nicht.
 */
class ServiceServerContext {

    /**
     * Generator für eindeutige Session-IDs.
     */
    private final AtomicLong session_id_pool = new AtomicLong();

    /**
     * Generator für zufällige Token (threadsafe).
     */
    private final Random random_generator = new Random();

    /**
     * Map, mit der die Session-IDs auf die Session gemappt wird.
     */
    private final Map<Long, SessionInfo> sessions = new ConcurrentHashMap<>();

    /**
     * Erzeugt eine neue Session. Jede Session ist über die Laufzeit des
     * Servers hinweg eindeutig.
     *
     * @return eine gültige, neue Session.
     */
    public SessionInfo newSession() {
        var session_id = session_id_pool.incrementAndGet();
        var token = random_generator.nextLong();
        var session = new SessionInfo(session_id, token);
        sessions.put(session.id(), session);
        return session;
    }

    /**
     * Entfernt die Session aus der Liste der aktiven Sessions.
     *
     * @param session Die zu löschende Session.
     */
    public void removeSession(SessionInfo session) {
        sessions.remove(session.id());
    }

    /**
     * Prüft, ob es sich bei der gegebenen SessionInfo um eine gültige,
     * aktive Session handelt.
     *
     * @param session
     * @return
     */
    public boolean isValidSession(SessionInfo session) {
        var s = sessions.get(session.id());
        return s != null && s.token() == session.token();
    }

}
