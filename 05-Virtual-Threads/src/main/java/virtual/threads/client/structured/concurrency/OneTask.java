package virtual.threads.client.structured.concurrency;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class OneTask {

    public static <T> T oneOfTasks(
        List<Callable<T>> tasks,
        Duration duration
    ) throws InterruptedException, ExecutionException, TimeoutException {
        var deadline = Instant.now().plus(duration);
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<T>()) {
            List<? extends Supplier<T>> suppliers = tasks.stream()
                .map(scope::fork)
                .toList()
            ;
            scope.joinUntil(deadline);
            return scope.result();
        }
    }
}
