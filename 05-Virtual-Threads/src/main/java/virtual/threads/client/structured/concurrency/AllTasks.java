package virtual.threads.client.structured.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public class AllTasks {

    public static <T> List<T> allTasks(
        List<Callable<T>> tasks
    ) throws InterruptedException, ExecutionException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<? extends Supplier<T>> suppliers = tasks.stream()
                .map(scope::fork)
                .toList()
            ;
            scope.join();
            scope.throwIfFailed();
            return suppliers.stream().map(Supplier::get).toList();
        }
    }

}
