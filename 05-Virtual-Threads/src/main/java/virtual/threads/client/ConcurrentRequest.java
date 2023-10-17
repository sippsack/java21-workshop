package virtual.threads.client;

import virtual.threads.server.ServerId;
import virtual.threads.server.Service;
import virtual.threads.weather.Weather;
import virtual.threads.weather.WeatherException;
import virtual.threads.weather.WeatherService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class ConcurrentRequest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(getWeather("Erlangen"));
        System.out.println(getWeather("Köln"));
    }

    private static Weather getWeather(String area) throws InterruptedException, ExecutionException {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Weather>()) {
            scope.fork(() -> getWeather(
                new ServerId(1), // WeatherService.SERVICE_WITH_40_MS_DELAY,
                area
            ));
            scope.fork(() -> getWeather(
                new ServerId(2), // WeatherService.SERVICE_WITH_40_MS_DELAY,
                area
            ));

            return scope.join().result();
        }
    }

    public static Weather getWeather(
            ServerId id,
            String area
    ) throws IOException, ClassNotFoundException, WeatherException {
        return getWeather(id, area, LocalDateTime.now());
    }

    public static Weather getWeather(
        ServerId id,
        String area,
        LocalDateTime time
    ) throws IOException, ClassNotFoundException, WeatherException {
        var name = "fred";
        var password = Service.passwordForName(name);
        try (var session = WeatherService.login(id, name, password)) {
            return session.requestWeather(area, time);
        }
    }

}
