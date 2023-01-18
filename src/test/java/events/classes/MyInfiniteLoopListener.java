package events.classes;


import com.github.kardzhaliyski.events.EventListener;

public class MyInfiniteLoopListener {

    @EventListener
    public Integer event(Integer obj) {
        return obj + 1;
    }
    @EventListener
    public String event(Boolean obj) {
        return "Something";
    }

    @EventListener
    public Boolean event2(String obj) {
        return true;
    }
}
