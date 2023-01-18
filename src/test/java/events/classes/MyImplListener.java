package events.classes;


import com.github.kardzhaliyski.events.ApplicationEvent;
import com.github.kardzhaliyski.events.ApplicationListener;

public class MyImplListener implements ApplicationListener<ApplicationEvent> {

    public Object eventSourceReceived = null;
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        eventSourceReceived = event.getSource();
    }
}
