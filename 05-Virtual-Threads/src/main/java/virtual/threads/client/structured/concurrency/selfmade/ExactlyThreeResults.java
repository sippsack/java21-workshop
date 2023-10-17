
package virtual.threads.client.structured.concurrency.selfmade;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class ExactlyThreeResults<T, R> extends ExactlyThreeTaskScope<T, R> {

    public ExactlyThreeResults(Function<T, R> mapper) {
        super(mapper);
    }

    @Override
    public <U extends T> Subtask<U> fork(Callable<? extends U> task) {
        return super.fork(task);
    }

    @Override
    protected void handleComplete(Subtask<? extends T> subtask) {
        super.handleComplete(subtask);
    }

    @Override
    public List<R> result() throws NoSuchElementException {
        ensureOwnerAndJoined();
        return null;
    }

}
