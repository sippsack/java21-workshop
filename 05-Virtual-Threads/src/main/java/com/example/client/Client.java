
package com.example.client;

import com.example.server.ServerId;
import com.example.server.WeatherService;
import com.example.server.protocol.SessionInfo;
import com.example.weather.WeatherRequest;

public class Client {

    public static void main(String[] args) throws Exception {
        var id = new ServerId(1);
        
        var name = "fred";
        var password = WeatherService.passwordForName(name);
                
        var session = WeatherService.login(id, name, password);
        System.out.println(session);
        var response = WeatherService.request(
            id,
            new WeatherRequest(session, "Erlangen")
        );
        System.out.println(response);
        
        WeatherService.logout(id, session);
        var response2 = WeatherService.request(
            id,
            new WeatherRequest(session, "Erlangen")
        );
        System.out.println(response2);
        
        var response3 = WeatherService.request(
            id,
            new WeatherRequest(SessionInfo.NO_INFO, "Erlangen")
        );
        System.out.println(response3);
    }
    
}
