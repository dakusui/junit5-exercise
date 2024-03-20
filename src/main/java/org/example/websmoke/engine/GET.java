package org.example.websmoke.engine;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, })
@Retention(RetentionPolicy.RUNTIME)
@Testable
public @interface GET {

    int expected();

}