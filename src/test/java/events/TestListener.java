package events;

import com.github.kardzhaliyski.Container;
import com.github.kardzhaliyski.classes.B;
import com.github.kardzhaliyski.events.ApplicationEvent;
import com.github.kardzhaliyski.events.ApplicationEventPublisher;
import events.classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class TestListener {
    Container r;

    @BeforeEach
    public void init() {
        r = new Container();
    }

    @Test
    public void testGetPublisherInstance() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        assertNotNull(publisher);
    }

    @Test
    public void testPublishEvent() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        MyListener listener = r.getInstance(MyListener.class);

        String event = "event";
        assertNotEquals(event, listener.receivedObject);
        publisher.publishEvent(event);
        assertEquals(event, listener.receivedObject);
    }

    @Test
    void testMultipleListenersReceivePublishedObject() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        MyListener listener = r.getInstance(MyListener.class);
        MyListener2 listener2 = r.getInstance(MyListener2.class);

        String event = "event";
        assertNotEquals(event, listener.receivedObject);
        assertNotEquals(event, listener2.receivedObject);
        publisher.publishEvent(event);
        assertEquals(event, listener.receivedObject);
        assertEquals(event, listener2.receivedObject);
    }

    @Test
    void testListenerReceiveOnlySpecificTypePublishedObject() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        MyStringListener listener = r.getInstance(MyStringListener.class);

        Long obj1 = 33L;
        assertNotEquals(obj1, listener.receivedObject);
        publisher.publishEvent(obj1);
        assertNotEquals(obj1, listener.receivedObject);

        String obj2 = "event";
        assertNotEquals(obj2, listener.receivedObject);
        publisher.publishEvent(obj2);
        assertEquals(obj2, listener.receivedObject);
    }

    @Test
    void testSingleThreadPublisher() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        MySlowListener listener = r.getInstance(MySlowListener.class);
        publisher.publishEvent("event");

        assertNotNull(listener.event1Finished);
        assertNotNull(listener.event2Finished);
        assertTrue(listener.event1Finished.toNanoOfDay() >= listener.event2Started.toNanoOfDay() ||
                listener.event2Finished.toNanoOfDay() >= listener.event1Started.toNanoOfDay());
    }

    @Test
    void testInfiniteLoopListener() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        MyInfiniteLoopListener listener = r.getInstance(MyInfiniteLoopListener.class);

        Throwable exception = null;
        try {
            publisher.publishEvent("Something");
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause.getClass() == InvocationTargetException.class) {
                exception = cause.getCause();
            } else {
                exception = cause;
            }
        } catch (StackOverflowError e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(StackOverflowError.class, exception.getClass());
    }

    @Test
    void testListenerCanRepeatOnItself() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        MyInfiniteLoopListener listener = r.getInstance(MyInfiniteLoopListener.class);

        Throwable exception = null;
        try {
            publisher.publishEvent(0);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause.getClass() == InvocationTargetException.class) {
                exception = cause.getCause();
            } else {
                exception = cause;
            }
        } catch (StackOverflowError e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(StackOverflowError.class, exception.getClass());
    }

    @Test
    void testResendingListener() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        MyResendingListener listener = r.getInstance(MyResendingListener.class);

        assertFalse(listener.success);
        publisher.publishEvent(3);
        assertTrue(listener.success);
    }

    @Test
    void testImplListenerReceivePublishedObject() throws Exception {
        ApplicationEventPublisher publisher = r.getInstance(ApplicationEventPublisher.class);
        MyImplListener listener = r.getInstance(MyImplListener.class);

        String event = "event";
        assertNotEquals(event, listener.eventSourceReceived);
        publisher.publishEvent(new ApplicationEvent(event));
        assertEquals(event, listener.eventSourceReceived);
    }
}
