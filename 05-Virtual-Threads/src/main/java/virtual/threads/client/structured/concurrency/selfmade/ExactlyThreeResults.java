
package virtual.threads.client.structured.concurrency.selfmade;

import virtual.threads.client.ConcurrentRequest;
import virtual.threads.weather.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static virtual.threads.weather.WeatherService.*;

public class ExactlyThreeResults<T, R> extends ExactlyThreeTaskScope<T, R> {

    public static void main(
        String[] args
    ) throws Exception {
        System.out.printf("Result: %.1f °C\n", getAverageTemperature("Erlangen"));
    }

    public static double getAverageTemperature(
            String area
    ) throws InterruptedException {
        try (var scope = new ExactlyThreeResults<>(Weather::getTemperature)) {
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_10_MS_DELAY, area));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_20_MS_DELAY, area));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_30_MS_DELAY, area));
//            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, area));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, area));
            scope.fork(() -> ConcurrentRequest.getWeather(SERVICE_WITH_ERROR, area));

            scope.join();
            return scope.result().stream().mapToDouble(Double::doubleValue).average().orElseThrow();
        }
    }

    private final List<R> results;

    private int success_count;

    private int total_count;

    private int error_count;

    public ExactlyThreeResults(Function<T, R> mapper) {
        super(mapper);
        results = new ArrayList<>(3);
        success_count = 0;
        total_count = 0;
        error_count = 0;
    }

    @Override
    public <U extends T> Subtask<U> fork(Callable<? extends U> task) {
        synchronized (this) {
            total_count++;
        }
        return super.fork(task);
    }

    @Override
    protected void handleComplete(Subtask<? extends T> subtask) {
        switch (subtask.state()) {
        case SUCCESS:
            synchronized (this) {
                success_count++;
                if (success_count <= 3) {
                    var result = mapper.apply(subtask.get());
                    results.add(result);
                    System.out.printf("Success: %s\n", result);
                }
                if (success_count == 3) {
                    shutdown();
                    System.out.println("Shutdown (SUCCESS)");
                }
            }
            break;
        case FAILED:
            System.out.println("Failure " + subtask.exception());
            synchronized (this) {
                error_count++;
                if (total_count - error_count < 3) {
                    // shutdown();
                    System.out.println("Shutdown (ERROR)");
                }
            }
            break;
        case UNAVAILABLE:
            ;
        }
    }

    public List<R> result() throws NoSuchElementException {
        ensureOwnerAndJoined();
        if (success_count < 3) {
            throw new NoSuchElementException("Only " + success_count + " results available");
        }
        return results;
    }

}
