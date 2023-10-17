
package virtual.threads.server;

import virtual.threads.server.protocol.*;
import virtual.threads.server.protocol.user.LoginRequest;
import virtual.threads.server.protocol.user.LoginResponse;
import virtual.threads.server.protocol.user.LogoutRequest;
import virtual.threads.server.protocol.user.LogoutResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceServer {

    private static final Logger LOG = Logger.getLogger(ServiceServer.class.getName());
        
    private static final int MAX_SERVERS = 1100;
    private static final int PORT_OFFSET = 10_000;
    
    private static final ServiceServerContext context = new ServiceServerContext();
    
    protected final String id;

    private final int port;

    private final int delay;

    private volatile boolean shut_down;
    
    public ServiceServer(int id, int delay) {
        checkId(id);
        this.port = idToPort(id);
        this.delay = delay;
        this.id = "Server#" + String.format("%04d", id);
        this.shut_down = false;
    }
    
    public static void checkId(int id) {
        if (id < 1 || id > MAX_SERVERS) {
            throw new IllegalArgumentException(
                "Für die ID muss 1 ≤ Id ≤ " + MAX_SERVERS + " gelten"
            );
        }
    }
    
    public static int idToPort(int id) {
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
            LOG.log(Level.SEVERE, "Can not connect WeatherService to port " + port, ex);
        }
    }
    
    private void serve(Socket s) {
        Response response;
        try (
            var socket = s;
            var in = new ObjectInputStream(socket.getInputStream());
            var out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            try {
                response = handleRequest(in);
            } catch (IOException e) {
                response = new ErrorResponse(
                    id,
                    Reason.IO_ERROR,
                    e.getMessage()
                );
            }
            out.writeObject(response);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    protected Response handleRequest(Request request) {
        return null;
    }

    public Response handleRequest(ObjectInputStream in) throws IOException {
        // Die Methode ignoriert IOExceptions, die im Aufrufer behandelt werden.
        // Zunächst wird das Objekt eingelesen. Der einzige zusätzliche Fehler neben
        // etwaiger I/0-Probleme ist ein unbekannter Objekttyp, für den keine Klasse
        // bekannt ist.
        Request request;
        try {
            request = (Request) in.readObject();
        } catch (ClassNotFoundException ex) {
            return new ErrorResponse(
                id,
                Reason.UNKNOWN_CLASS,
                ex.getMessage()
            );
        }

        // Falls es eine dem Request zugeordnete Session gibt, dann muss geprüft werden, ob
        // diese noch gültig ist. Ist das nicht der Fall, so muss der Request abgelehnt werden.
        var session = request.session();
        if (!session.equals(SessionInfo.NO_INFO)) {
            if (!context.isValidSession(session)) {
                return new ErrorResponse(
                    id,
                    Reason.ACCESS_DENIED,
                    session.toString()
                );
            }
        }

        // Zuerst wird geprüft, ob es sich bei einem der Requests um einen handelt, der rein
        // administrativ ist.
        switch (request) {
        case LoginRequest  r:
            return handleLoginRequest(r);
        case LogoutRequest r:
            return handleLogoutRequest(r);
        default:
            if (delay == -1) {
                return new ErrorResponse(
                    id,
                    Reason.ERROR_REQUEST,
                    request.session().toString()
                );
            }
            break;
        }

        // Falls ein Delay angegeben wurde, dann wird an dieser Stelle gewartet – bevor der
        // Dienst ausgeführt wird.
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ignored) {
                return new ErrorResponse(
                    id,
                    Reason.INTERRUPTED,
                    request.session().toString()
                );
            }
        }

        // Dem ableitenden Server wird nun die Möglichkeit gegeben, den Request zu behandeln.
        // Dieser liefert null, wenn er mit dem Request nichts anfangen konnte. Andernfalls
        // wird das Ergebnis geliefert.
        // Da die überschreibende Methode nicht bekannt ist, werden alle Ausnahmen abgefangen
        // und als Fehler weitergereicht.
        try {
            var response = handleRequest(request);
            if (response == null) {
                response = new ErrorResponse(
                     id,
                     Reason.UNKNOWN_REQUEST,
                     request.getClass().getName()
                 );
            }
            return response;
        } catch (Throwable e) {
            LOG.log(Level.SEVERE, null, e);
            return new ErrorResponse(
                id,
                Reason.SERVER_ERROR,
                e.getMessage()
            );
        }
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

