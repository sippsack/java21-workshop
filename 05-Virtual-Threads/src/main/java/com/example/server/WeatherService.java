
package com.example.server;

import com.example.server.protocol.ErrorResponse;
import com.example.server.protocol.Request;
import com.example.server.protocol.Response;
import com.example.server.protocol.SessionInfo;
import com.example.server.protocol.user.LoginRequest;
import com.example.server.protocol.user.LoginResponse;
import com.example.server.protocol.user.LogoutRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class WeatherService {

	/**
	 * Ein Wetterdienst, der erst nach 10 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_10_MS_DELAY   = new ServerId(1001,  10);
    
	/**
	 * Ein Wetterdienst, der erst nach 20 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_20_MS_DELAY   = new ServerId(1002,  20);
    
	/**
	 * Ein Wetterdienst, der erst nach 30 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_30_MS_DELAY   = new ServerId(1003,  30);
    
	/**
	 * Ein Wetterdienst, der erst nach 40 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_40_MS_DELAY   = new ServerId(1004,  40);
    
	/**
	 * Ein Wetterdienst, der erst nach 50 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_50_MS_DELAY   = new ServerId(1005,  50);
    
	/**
	 * Ein Wetterdienst, der erst nach 60 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_60_MS_DELAY   = new ServerId(1006,  60);
    
	/**
	 * Ein Wetterdienst, der erst nach 70 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_70_MS_DELAY   = new ServerId(1007,  70);
    
	/**
	 * Ein Wetterdienst, der erst nach 80 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_80_MS_DELAY   = new ServerId(1008,  80);
    
	/**
	 * Ein Wetterdienst, der erst nach 90 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_90_MS_DELAY   = new ServerId(1009,  90);
    
	/**
	 * Ein Wetterdienst, der erst nach 100 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_100_MS_DELAY  = new ServerId(1010, 100);
    
	/**
	 * Ein Wetterdienst, der erst nach 200 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_200_MS_DELAY  = new ServerId(1011, 200);

	/**
	 * Ein Wetterdienst, der erst nach 300 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_300_MS_DELAY 	= new ServerId(1012, 300);

	/**
	 * Ein Wetterdienst, der nie antwortet.
	 */
    public static final ServerId SERVICE_WITH_MAX_DELAY		= new ServerId(1013, Integer.MAX_VALUE);

    public static Response request(
        ServerId id, 
        Request request
    ) throws 
        IOException, 
        UnknownHostException, 
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
        WeatherServiceServer.checkId(id);
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
     * @param id Die ID des Service
     * @param name Der Benutzername
     * @param password Das Kennwort für den angegebenen Benutzer
     * 
     * @return Die Session, über den der Zugang zu den Services gewährt wird.
     * 
     * @throws IOException
     * @throws UnknownHostException
     * @throws ClassNotFoundException
     */
    public static SessionInfo login(
        ServerId id, 
        String name, 
        char[] password
    ) throws         
        IOException, 
        UnknownHostException, 
        ClassNotFoundException 
    {
        return login(id.id(), name, password);
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
     * @throws IOException
     * @throws UnknownHostException
     * @throws ClassNotFoundException
     */
    public static SessionInfo login(
        int id, 
        String name, 
        char[] password
    ) throws 
        IOException, 
        UnknownHostException, 
        ClassNotFoundException 
    {
        System.out.println("TRYING LOGIN " + name);
        var reply = WeatherService.request(
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
     * @throws IOException
     * @throws UnknownHostException
     * @throws ClassNotFoundException
     */
    public static void logout(
        ServerId id, 
        SessionInfo session
    ) throws 
        IOException, 
        UnknownHostException, 
        ClassNotFoundException 
    {
        var reply = WeatherService.request(
            id, 
            new LogoutRequest(session),
            0
        );
        switch (reply) {
            case ErrorResponse e -> {
                System.out.println(e);
            }
            default -> {}
        };
    }
    
}

