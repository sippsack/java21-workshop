package virtual.threads;


import org.junit.platform.commons.logging.LoggerFactory;

public class Simple {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> System.out.println(Thread.currentThread().threadId());
        new Thread(runnable).run();
        Thread.ofPlatform().start(runnable);
        Thread.ofVirtual().start(runnable);
        Thread.sleep(1000);
    }
}
