
package com.example.server;

import com.example.server.protocol.ErrorResponse;
import com.example.server.protocol.Reason;
import com.example.server.protocol.Request;
import com.example.server.protocol.Response;
import com.example.server.protocol.SessionInfo;
import com.example.server.protocol.user.LoginRequest;
import com.example.server.protocol.user.LoginResponse;
import com.example.server.protocol.user.LogoutRequest;
import com.example.server.protocol.user.LogoutResponse;
import com.example.weather.WeatherRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherServiceServer {

    private static final Logger LOG = Logger.getLogger(WeatherServiceServer.class.getName());
        
    private static final int MAX_SERVERS = 1100;
    private static final int PORT_OFFSET = 10_000;
    
    private static final ServiceContext context = new ServiceContext();
    
    private final String id;
    private final int port;
    private final int delay;
    private volatile boolean shut_down;
    
    public WeatherServiceServer(int id, int delay) {
        checkId(id);
        this.port = idToPort(id);
        this.delay = delay;
        this.id = "Server#" + String.format("%04d", id);
    }
    
    static void checkId(int id) {
        if (id < 1 || id > MAX_SERVERS) {
            throw new IllegalArgumentException(
                "Für die ID muss 1 ≤ Id ≤ " + MAX_SERVERS + " gelten"
            );
        }
    }
    
    static int idToPort(int id) {
        return PORT_OFFSET + id;
    }
    
    public void run() {
        try (var server_socket = new ServerSocket(port)) {
            while (!shut_down) {
                try {
                    LOG.log(Level.INFO, () -> id + " is listening");
                    var s = server_socket.accept();
                    Thread.ofVirtual().start(() -> serve(s));
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                } catch (Throwable ex) {
                    LOG.log(Level.SEVERE, "Unexpected Exception", ex);
                }
            }
        } catch (IOException ex) {
            
        }
    }
    
    private void serve(Socket s) {
        Response response = null;
        try (
    		var socket = s;
            var in = new ObjectInputStream(socket.getInputStream()); 
            var out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            Request request = null;
            try {
                request = (Request) in.readObject();
            } catch (ClassNotFoundException ex) {
                response = new ErrorResponse(
                    id, 
                    Reason.UNKNOWN_CLASS, 
                    ex.getMessage()
                );
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            if (delay != 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    ;
                }
            }
            if (request != null) {
                var session = request.session();
                if (!session.equals(SessionInfo.NO_INFO)) {
                    if (!context.isValidSession(session)) {
                        response = new ErrorResponse(
                            id, 
                            Reason.ACCESS_DENIED, 
                            session.toString()
                        );
                        request = null;
                    }
                } else {
                	switch (request) {
                	case LoginRequest r:
                		break;
            		default:
                        response = new ErrorResponse(
	                         id, 
	                         Reason.UNKNOWN_SESSION, 
	                         session.toString()
	                     );
	                     request = null;
                	}
                }
            }
            try {
                response = switch (request) {
                    case null -> response;
                    case WeatherRequest	r -> handleWeatherRequest(r);
                    case LoginRequest 	r -> handleLoginRequest(r);
                    case LogoutRequest  r -> handleLogoutRequest(r);
                    default -> new ErrorResponse(
                        id, 
                        Reason.UNKNOWN_REQUEST, 
                        request.getClass().getName()
                    );                    
                };
            } catch (Throwable ex) {
                response = new ErrorResponse(
                    id, 
                    Reason.SERVER_ERROR, 
                    ex.getMessage()
                );
                LOG.log(Level.SEVERE, null, ex);
            }
            try {
                out.writeObject(response);                 
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    private Response handleWeatherRequest(WeatherRequest request) {
        return new ErrorResponse(
            id, 
            Reason.ILLEGAL_PARAMETER, 
            request.area() + ": " + request.time()
        );
    }
    
    private Response handleLoginRequest(LoginRequest request) {
        var name = request.name();
        var password = request.password();
        if (name.isEmpty() || name.isBlank()) {
            Arrays.fill(password, '0');
            return new ErrorResponse(
                id, 
                Reason.ILLEGAL_PARAMETER, 
                "name must not be empty or blank"
            );
        }
        var hash = 0;
        for (int i = 0, n = password.length; i < n; i++) {
            hash = hash * 10 + (password[i] - '0');
            password[i] = 0;
        }
        System.out.println("HASH: " + hash);
        if (name.hashCode() != hash) {
            return new ErrorResponse(
                id, 
                Reason.LOGIN_FAILED, 
                "incorrect name/password combination"
            );
        }
        return new LoginResponse(id, context.newSession());
    }
    
    private Response handleLogoutRequest(LogoutRequest request) {
        var session = request.session();
        context.removeSession(session);
        return new LogoutResponse(id);
    }
    
}

/**
 * Verwaltet die einzelnen Sessions und kann etwa Auskunft darüber geben,
 * ob ein Request zulässig ist oder nicht.
 */
class ServiceContext {
    
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
     * Servers hinwegeindeutig.
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
     * @param session
     */
    public void removeSession(SessionInfo session) {
        sessions.remove(session.id());
    }
    
    /**
     * Prüft, ob es sich bei der gegebenen SessionInfo um eine gültige, 
     * aktive Session handelt. 
     * 
     * 
     * @param session
     * @return
     */
    public boolean isValidSession(SessionInfo session) {
        var s = sessions.get(session.id());
        return s != null && s.token() == session.token();
    }
    
}
