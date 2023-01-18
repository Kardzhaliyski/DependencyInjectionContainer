package events.classes;


import com.github.kardzhaliyski.events.EventListener;

import java.time.LocalTime;


public class MySlowListener {

    public LocalTime event1Started = null;
    public LocalTime event1Finished = null;
    public LocalTime event2Finished = null;
    public LocalTime event2Started = null;

    @EventListener
    public void event1(Object obj) throws InterruptedException {
        event1Started = LocalTime.now();
        task();
        event1Finished = LocalTime.now();
    }

    @EventListener
    public void event2(Object obj) throws InterruptedException {
        event2Started = LocalTime.now();
        task();
        event2Finished = LocalTime.now();
    }

    private static void task() {
        String str = "";
        for (int i = 0; i < 100000; i++) {
            str = str + i;
            if(i % 100 == 0) {
                str = str.substring(str.length() / 2);
            }
        }
    }
}
