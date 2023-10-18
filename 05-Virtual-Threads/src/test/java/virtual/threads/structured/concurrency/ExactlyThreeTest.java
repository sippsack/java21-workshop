package virtual.threads.structured.concurrency;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import virtual.threads.client.ConcurrentRequest;
import virtual.threads.client.structured.concurrency.selfmade.ExactlyThreeResults;
import virtual.threads.client.structured.concurrency.selfmade.ExactlyThreeTaskScope;
import virtual.threads.weather.Weather;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import static virtual.threads.weather.WeatherService.*;

public class ExactlyThreeTest {

    private static final String AREA = "Erlangen";

    private static final LocalDateTime TIME = LocalDateTime.of(2023, 10, 18, 12, 0);

    private ExactlyThreeTaskScope<Weather, Double> createTaskScope() {
        return new ExactlyThreeResults<>(Weather::getTemperature);
    }

    @Test
    public void testNoneOfFiveSuccessful() throws InterruptedException {
        try (var scope = createTaskScope()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));

            scope.join();
            Assertions.assertThrows(NoSuchElementException.class, scope::result);
        }
    }

    @Test
    public void testOneSuccessful() throws InterruptedException {
        try (var scope = createTaskScope()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));

            scope.join();
            Assertions.assertThrows(NoSuchElementException.class, scope::result);
        }
    }

    @Test
    public void testTwoSuccessful() throws InterruptedException {
        try (var scope = createTaskScope()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));

            scope.join();
            Assertions.assertThrows(NoSuchElementException.class, scope::result);
        }
    }

    @Test
    public void testThreeSuccessful() throws InterruptedException {
        try (var scope = createTaskScope()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));

            scope.join();
            var result = scope.result();
            Assertions.assertEquals(3, result.size());
        }
    }

    @Test
    public void testFourSuccessful() throws InterruptedException {
        try (var scope = createTaskScope()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));

            scope.join();
            var result = scope.result();
            Assertions.assertEquals(3, result.size());
        }
    }

//    @Test
    public void testSuccessfulEvenWithEndlessDelay() throws InterruptedException {
        try (var scope = createTaskScope()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_NO_DELAY, AREA, TIME));

            scope.join();
            var result = scope.result();
            Assertions.assertEquals(3, result.size());
        }
    }

    @Test
    public void testWithEndlessDelays() {
        try (var scope = createTaskScope()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));

            var deadline = Instant.now().plus(Duration.ofSeconds(3));
            Assertions.assertThrows(TimeoutException.class, () -> scope.joinUntil(deadline));
            Assertions.assertThrows(IllegalStateException.class, scope::result);
        }
    }

    /**
     * Extra-Aufgabe: Stellen Sie sicher, dass auch schon dann ein Ergebnis geliefert bzw. eine
     * Ausnahme geworfen wird, wenn genügend Fehler darüber entscheiden, ob die drei
     * erforderlichen Ergebnisse überhaupt noch möglich sind.
     * Dazu bitte die mit (1) markierten Zeilen löschen und die mit (2) markierte Zeile aktivieren.
     */
    @Test
    public void testWithEnoughErrorsAndEndlessDelays() throws InterruptedException {
        try (var scope = createTaskScope()) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_MAX_DELAY, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, AREA, TIME));

            scope.join();
            Assertions.assertThrows(NoSuchElementException.class, scope::result);

        }
    }

}
