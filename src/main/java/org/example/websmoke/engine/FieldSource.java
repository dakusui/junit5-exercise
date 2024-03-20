package org.example.websmoke.engine;

import org.junit.platform.engine.TestSource;

import java.lang.reflect.Field;

public class FieldSource implements TestSource {

    private static final long serialVersionUID = 1L;

    private final String className;
    private final String fieldName;


    public static FieldSource from(Field testField) {
        return new FieldSource(testField.getDeclaringClass().toGenericString(), testField.getName());
    }

    public FieldSource(String className, String fieldName) {
        this.className = className;
        this.fieldName = fieldName;
    }

}
