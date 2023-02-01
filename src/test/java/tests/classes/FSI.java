package tests.classes;

import com.github.kardzhaliyski.annotations.Initializer;
import com.github.kardzhaliyski.annotations.Autowire;
import com.github.kardzhaliyski.annotations.Qualifier;

public class FSI implements Initializer {
    @Autowire
    @Qualifier
    public
    String email;

    @Override
    public void init() throws Exception {
        email = "mailto:" + email;
    }
}
