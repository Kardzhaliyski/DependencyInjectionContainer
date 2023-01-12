package com.github.kardzhaliyski.classes;

import com.github.kardzhaliyski.annotations.Inject;
import com.github.kardzhaliyski.annotations.Named;
import com.github.kardzhaliyski.annotations.NamedParameter;

import java.beans.ConstructorProperties;

public class EN {
    public A aField;

    @Inject
    public EN(@NamedParameter("aNamedField") A aNamedField) {
        this.aField = aNamedField;
    }
}
