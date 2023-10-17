
package virtual.threads;

import virtual.threads.number.NumberService;
import virtual.threads.number.intern.NumberServiceServer;
import virtual.threads.server.ServerId;
import virtual.threads.server.ServiceServer;
import virtual.threads.weather.WeatherService;
import virtual.threads.weather.intern.WeatherServiceServer;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class SetupServer {

    private static final int N = 10;
    private static final ArrayList<Thread> threads = new ArrayList<>(N);
    
    public static void main(String[] args) throws InterruptedException {
        createWeatherServices();
        createNumberServices();
        join();
    }

    private static void createWeatherServices() {
        // Einen benannten Standard-Service starten.
        createWeatherService(WeatherService.SERVICE_WITH_NO_DELAY);

        // Die N-1 verbliebenen Standard-Services starten.
        for (int i = 2; i <= 10; i++) {
            createWeatherService(i, 0);
        }

        // Die benannten Services starten.
        createWeatherService(WeatherService.SERVICE_WITH_10_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_20_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_30_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_40_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_50_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_60_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_70_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_80_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_90_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_100_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_200_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_300_MS_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_MAX_DELAY);
        createWeatherService(WeatherService.SERVICE_WITH_ERROR);
    }

    private static void createNumberServices() {
        // Einen benannten Standard-Service starten.
        createNumberService(NumberService.SERVICE_WITH_NO_DELAY);

        // Die N-1 verbliebenen Standard-Services starten.
        for (int i = 22; i <= 30; i++) {
            createNumberService(i, 0);
        }

        // Die benannten Services starten.
        createNumberService(NumberService.SERVICE_WITH_10_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_20_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_30_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_40_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_50_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_60_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_70_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_80_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_90_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_100_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_200_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_300_MS_DELAY);
        createNumberService(NumberService.SERVICE_WITH_MAX_DELAY);
        createNumberService(NumberService.SERVICE_WITH_ERROR);
    }

    private static void createNumberService(ServerId id) {
        createNumberService(id.id(), id.delay());
    }

    private static void createNumberService(int id, int delay) {
        createService(id, delay, NumberServiceServer::new);
    }

    private static void createWeatherService(ServerId id) {
        createWeatherService(id.id(), id.delay());
    }
    
    private static void createWeatherService(int id, int delay) {
        createService(id, delay, WeatherServiceServer::new);
    }

    private static void createService(
        int id,
        int delay,
        BiFunction<Integer, Integer, ServiceServer> creator
    ) {
        var t = Thread.ofVirtual().start(
            () -> creator.apply(id, delay).run()
        );
        threads.add(t);
    }

    private static void join() throws InterruptedException {
        for (var t : threads) {
            t.join();
        }        
    }
    
    
}
