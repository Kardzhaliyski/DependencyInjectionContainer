package com.github.kardzhaliyski.classes;

import com.github.kardzhaliyski.annotations.Inject;

public class E {
    public A aField;

    @Inject
    public E(A aField) {
        this.aField = aField;
    }
}
