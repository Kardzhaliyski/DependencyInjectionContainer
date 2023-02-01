package asyncs;

import com.github.kardzhaliyski.annotations.Async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AsyncMethods {
    public static String threadName;
    public static String VALUE = "Some Value";

    @Async
    public void asyncMethodWithVoidReturnType() {
        threadName = Thread.currentThread().getName();
    }

    @Async
    public Future<String> asyncMethodWithReturnType() {
        CompletableFuture<String> future = new CompletableFuture<>();
        threadName = Thread.currentThread().getName();
        try {
            TimeUnit.SECONDS.sleep(1);
            future.complete(VALUE);
        } catch (InterruptedException e) {
            //
        }

        return future;
    }
}
