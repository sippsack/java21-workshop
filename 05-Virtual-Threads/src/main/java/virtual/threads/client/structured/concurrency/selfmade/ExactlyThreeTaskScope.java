
package virtual.threads.client.structured.concurrency.selfmade;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Function;

public abstract class ExactlyThreeTaskScope<T, R> extends StructuredTaskScope<T> {

    protected final Function<T, R> mapper;

    protected ExactlyThreeTaskScope(Function<T, R> mapper) {
        this.mapper = mapper;
    }

    public abstract List<R> result() throws NoSuchElementException;

}
