package events.classes;


import com.github.kardzhaliyski.events.EventListener;

public class MyResendingListener {

    public Boolean success = false;
    @EventListener
    public Boolean event(Integer value) {
        return true;
    }

    @EventListener
    public void event(Boolean value) {
        this.success = value;
    }
}
