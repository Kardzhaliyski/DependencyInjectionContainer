package asyncs;

import com.github.kardzhaliyski.Container;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    static Container r;
    static AsyncMethods asyncMethodsObject;

    @BeforeAll
    static void init() throws Exception {
        r = new Container();
        asyncMethodsObject = r.getInstance(AsyncMethods.class);
    }

    @Test
    void test() throws InterruptedException {
        asyncMethodsObject.asyncMethodWithVoidReturnType();
        TimeUnit.SECONDS.sleep(1);
        String methodThreadName = asyncMethodsObject.threadName;
        String currentThreadName = Thread.currentThread().getName();
        assertNotEquals(currentThreadName, methodThreadName);
    }

    @Test
    public void testAsyncForMethodsWithReturnType() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println();
        Future<String> future = asyncMethodsObject.asyncMethodWithReturnType();

        assertFalse(future.isDone());
        String value = future.get(2, TimeUnit.SECONDS);
        assertNotNull(value);
        assertEquals(AsyncMethods.VALUE, value);
    }


}
