package tests.classes;

import com.github.kardzhaliyski.annotations.Autowire;
import com.github.kardzhaliyski.annotations.Lazy;

public class L {
    @Autowire
    @Lazy
    public LA laField;
}
