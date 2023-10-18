
package virtual.threads.client.structured.concurrency.selfmade;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class ExactlyThreeResults<T, R> extends ExactlyThreeTaskScope<T, R> {

    private final List<R> results;

    private int total_count;

    private int success_count;

    private int error_count;

    public ExactlyThreeResults(Function<T, R> mapper) {
        super(mapper);
        results = new ArrayList<>(3);
        total_count = 0;
        success_count = 0;
        error_count = 0;
    }

    @Override
    public <U extends T> Subtask<U> fork(Callable<? extends U> task) {
        synchronized (results) {
            total_count++;
        }
        return super.fork(task);
    }

    @Override
    protected void handleComplete(Subtask<? extends T> subtask) {
        synchronized (results) {
            if (subtask.state() == Subtask.State.SUCCESS) {
                success_count++;
                results.add(mapper.apply(subtask.get()));
                if (success_count == 3) {
                    shutdown();
                }
            } else if (subtask.state() == Subtask.State.FAILED) {
                error_count++;
                if (total_count - error_count < 3) {
                    shutdown();
                }
            }
        }
    }

    @Override
    public List<R> result() throws NoSuchElementException {
        ensureOwnerAndJoined();
        if (results.size() != 3) {
            throw new NoSuchElementException("not enough results: " + results.size());
        }
        return results;
    }

}
