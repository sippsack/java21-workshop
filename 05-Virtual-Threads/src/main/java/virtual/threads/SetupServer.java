
package virtual.threads;

import virtual.threads.server.ServerId;
import virtual.threads.weather.WeatherService;
import virtual.threads.weather.intern.WeatherServiceServer;

import java.util.ArrayList;

public class SetupServer {

    private static final int N = 10;
    private static final ArrayList<Thread> threads = new ArrayList<>(N);
    
    public static void main(String[] args) throws InterruptedException {
        // Einen benannten Standard-Service starten.
        createThread(WeatherService.SERVICE_WITH_NO_DELAY);

        // Die N-1 verbliebenen Standard-Services starten.
        for (int i = 2; i <= 10; i++) {
            createThread(i, 0);
        }

        // Die benannten Services starten.
        createThread(WeatherService.SERVICE_WITH_10_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_20_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_30_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_40_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_50_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_60_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_70_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_80_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_90_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_100_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_200_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_300_MS_DELAY);
        createThread(WeatherService.SERVICE_WITH_MAX_DELAY);
        createThread(WeatherService.SERVICE_WITH_ERROR);

        join();
    }
    
    private static void createThread(ServerId id) {
        createThread(id.id(), id.delay());
    }
    
    private static void createThread(int id, int delay) {
        var t = Thread.ofVirtual().start(
            () -> new WeatherServiceServer(id, delay).run()
        );
        threads.add(t);
    }
    
    private static void join() throws InterruptedException {
        for (var t : threads) {
            t.join();
        }        
    }
    
    
}
