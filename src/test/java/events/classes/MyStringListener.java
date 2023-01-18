package events.classes;


import com.github.kardzhaliyski.events.EventListener;

public class MyStringListener {
    public Object receivedObject = null;
    @EventListener
    public void event(String obj) {
        receivedObject = obj;
    }
}
