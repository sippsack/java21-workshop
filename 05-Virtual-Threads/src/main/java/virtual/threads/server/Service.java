
package virtual.threads.server;

import virtual.threads.server.protocol.ErrorResponse;
import virtual.threads.server.protocol.Request;
import virtual.threads.server.protocol.Response;
import virtual.threads.server.protocol.SessionInfo;
import virtual.threads.server.protocol.user.LoginRequest;
import virtual.threads.server.protocol.user.LoginResponse;
import virtual.threads.server.protocol.user.LogoutRequest;
import virtual.threads.weather.intern.WeatherServiceServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Service {

    public static Response request(
        ServerId id, 
        Request request
    ) throws
        UnknownHostException,
        IOException,
        ClassNotFoundException,
        SocketTimeoutException 
    {
        return request(id.id(), request, 0);
    }
    
    public static Response request(
        int id, 
        Request request
    ) throws 
        IOException, 
        UnknownHostException, 
        ClassNotFoundException, 
        SocketTimeoutException 
    {
        return request(id, request, 0);
    }
    
    public static Response request(
        ServerId id, 
        Request request,
        int timeout
    ) throws 
        UnknownHostException, 
        IOException, 
        ClassNotFoundException, 
        SocketTimeoutException 
    {
        return request(id.id(), request, timeout);
    }
    
    public static Response request(
        int id, 
        Request request,
        int timeout
    ) throws 
        UnknownHostException, 
        IOException, 
        ClassNotFoundException, 
        SocketTimeoutException 
    {
        return request(id, request, timeout, null);
    }
    
    public static Response request(
        int id, 
        Request request,
        int timeout,
        Runnable action
    ) throws 
        UnknownHostException, 
        IOException, 
        ClassNotFoundException, 
        SocketTimeoutException 
    {
        ServiceServer.checkId(id);
        var address = Inet4Address.getLoopbackAddress();
        try (var socket = new Socket(address, WeatherServiceServer.idToPort(id))) {
	        if (timeout != 0) {
	            socket.setSoTimeout(timeout);
	        }
	        
	        try (
	            var out = new ObjectOutputStream(socket.getOutputStream());
	            var in = new ObjectInputStream(socket.getInputStream())
	        ) {
	            out.writeObject(request);
	            if (action != null) {
	                action.run();
	            }
	            return (Response) in.readObject();
	        }
        }
    }
    
    /**
     * Meldet den gegebenen Benutzer mit seinem Passwort bei dem gewünschten Service an.
     * 
     * @param id Die ID des Service (1, ..., 10)
     * @param name Der Benutzername
     * @param password Das Kennwort für den angegebenen Benutzer
     * 
     * @return Die Session, über den der Zugang zu den Services gewährt wird.
     *
     * @throws IOException wenn ein I/O-Fehler aufgetreten ist
     * @throws UnknownHostException der Server-Host nicht bekannt ist
     */
    public static SessionInfo login(
        int id, 
        String name, 
        char[] password
    ) throws 
        IOException, 
        UnknownHostException
    {
        try {
            var reply = Service.request(
                    id,
                    new LoginRequest(name, password),
                    0,
                    () -> Arrays.fill(password, '0')
            );
            return switch (reply) {
                case LoginResponse login -> login.session();
                case ErrorResponse e -> {
                    System.out.println(e);
                    yield SessionInfo.NO_INFO;
                }
                default -> null;
            };
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }
    
    /**
     * Liefert das Passwort für den angegebenen Benutzer.
     * 
     * @param name Der Benutzername
     * 
     * @return Das Passwort
     */
    public static char[] passwordForName(String name) {
    	return Integer.toString(name.hashCode()).toCharArray();
    }
    
    /**
     * Meldet die Session bei dem Server ab. Anschließend hat der Benutzer keinen Zugang mehr
     * zu dem Server.
     * 
     * @param id Der betroffene Server
     * @param session Die zu beendende Session
     * 
     * @throws IOException wenn ein I/O-Fehler aufgetreten ist
     * @throws UnknownHostException der Server-Host nicht bekannt ist
     */
    public static void logout(
        ServerId id, 
        SessionInfo session
    ) throws 
        IOException, 
        UnknownHostException
    {
        try {
            var reply = Service.request(
                    id,
                    new LogoutRequest(session),
                    0
            );
            if (reply instanceof ErrorResponse e) {
                System.out.println(e);
            }
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }
    
}
