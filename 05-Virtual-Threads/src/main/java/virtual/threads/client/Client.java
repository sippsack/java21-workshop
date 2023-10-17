
package virtual.threads.client;

import virtual.threads.server.Service;
import virtual.threads.weather.WeatherService;

import java.time.LocalDateTime;

public class Client {

    public static void main(String[] args) throws Exception {
        var server_id = WeatherService.SERVICE_WITH_NO_DELAY;

        var name = "fred";
        var password = Service.passwordForName(name);
                
        try (var session = WeatherService.login(server_id, name, password)) {
            System.out.println(session);

            for (int m = 1; m <= 12; m++) {
                for (int d = 1; d <= 28; d++) {
                    var t = LocalDateTime.of(2023, m, d, 12, 0);
                    var weather = session.requestWeather("Erlangen", t);
                    System.out.println(weather);
                }
            }

        }
//        session.logout();

//        var response2 = session.requestWeather("Erlangen");
//        System.out.println(response2);
//
//        var response3 = WeatherService.request(
//            server_id,
//            new WeatherRequest(SessionInfo.NO_INFO, "Erlangen")
//        );
//        System.out.println(response3);
    }
    
}
