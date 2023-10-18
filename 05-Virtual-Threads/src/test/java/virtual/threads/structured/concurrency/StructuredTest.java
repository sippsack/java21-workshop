package virtual.threads.structured.concurrency;

import org.junit.jupiter.api.Assertions;
import virtual.threads.client.ConcurrentRequest;
import virtual.threads.weather.Weather;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;

import static virtual.threads.weather.WeatherService.SERVICE_WITH_ERROR;
import static virtual.threads.weather.WeatherService.*;

public class StructuredTest {

    private static final String AREA = "Erlangen";
    private static final LocalDateTime TIME = LocalDateTime.of(
        2023,
        10,
        18,
        12,
        00
    );

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Weather>()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY_1, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));

            var deadline = Instant.now().plus(Duration.ofSeconds(3));

            scope.joinUntil(deadline);
            System.out.println(scope.result());
        }
    }

}
