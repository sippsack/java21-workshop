package virtual.threads;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import virtual.threads.number.NumberException;
import virtual.threads.number.NumberService;
import virtual.threads.server.Service;
import virtual.threads.weather.WeatherException;
import virtual.threads.weather.WeatherService;

import java.io.IOException;
import java.time.LocalDateTime;

public class SetupTest {

    @Test
    public void testNumberServiceSetup() throws IOException, NumberException {
        var name = "fred";
        var password = Service.passwordForName(name);

        try (var session = NumberService.login(NumberService.SERVICE_WITH_NO_DELAY, name, password)) {
            var number = session.calculateSquareRoot(16);
            Assertions.assertEquals(4.0, number);
        }
    }

    @Test
    public void testWeatherServiceSetup() throws IOException, WeatherException {
        var name = "fred";
        var password = Service.passwordForName(name);

        try (var session = WeatherService.login(WeatherService.SERVICE_WITH_NO_DELAY, name, password)) {
            System.out.println(session);
            for (int m = 10; m <= 10; m++) {
                for (int d = 1; d <= 28; d++) {
                    var t = LocalDateTime.of(2023, m, d, 12, 0);
                    var weather = session.requestWeather("Erlangen", t);
                    System.out.println(weather);
                }
            }
        }
    }

}
