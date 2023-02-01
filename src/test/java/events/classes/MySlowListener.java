package events.classes;


import com.github.kardzhaliyski.events.EventListener;

import java.time.LocalTime;


public class MySlowListener {


    public long event1Started = 0;
    public long event1Finished = 0;
    public long event2Started = 0;
    public long event2Finished = 0;

    @EventListener
    public void event1(String obj) {
        event1Started = System.currentTimeMillis();
        task();
        event1Finished = System.currentTimeMillis();
    }

    @EventListener
    public void event2(String obj) {
        event2Started = System.currentTimeMillis();
        task();
        event2Finished = System.currentTimeMillis();
    }

    private void task() {
        String str = "";
        for (int i = 0; i < 100000; i++) {
            str = str + Math.random();
            if (i % 100 == 0) {
                str = str.substring(str.length() / 2);
            }
        }
    }

    @EventListener
    public void appEvent1(MyApplicationEvent event) {
        event1Started = System.currentTimeMillis();
        task();
        event1Finished = System.currentTimeMillis();
    }

    @EventListener
    public void appEvent2(MyApplicationEvent event) {
        event2Started = System.currentTimeMillis();
        task();
        event2Finished = System.currentTimeMillis();
    }
}
