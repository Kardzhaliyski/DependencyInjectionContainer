package com.github.kardzhaliyski.classes;

import com.github.kardzhaliyski.annotations.Inject;
import com.github.kardzhaliyski.annotations.Named;

public class FSI implements Initializer {
    @Inject
    @Named
    public
    String email;

    @Override
    public void init() throws Exception {
        email = "mailto:" + email;
    }
}
