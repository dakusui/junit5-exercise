package org.example.websmoke.engine;

import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

import java.lang.reflect.Field;
import java.util.Optional;


public class FieldTestDescriptor extends AbstractTestDescriptor {

    private final Field testField;


    public FieldTestDescriptor(Field testField, ClassTestDescriptor parent) {
		super( //
				parent.getUniqueId().append("method", testField.getName()), //
                displayName(testField), //
				FieldSource.from(testField) //
		);
		this.testField = testField;
		setParent(parent);
    }

    public Field getTestField() {
        return testField;
    }


    @Override
    public Type getType() {
        return Type.TEST;
    }


    private static String displayName(Field testField) {
        return "GET " + getUrl(testField) + "  -> " + getExpectedStatusCode(testField);
    }

    static int getExpectedStatusCode(Field testField) {
        Optional<GET> annotation = AnnotationUtils.findAnnotation(testField, GET.class);
        return annotation.map(GET::expected).orElseThrow();
    }

    static String getUrl(Field testField)  {
        try {
            return testField.get(null).toString();
        } catch (IllegalAccessException e) {
            return "no url";
        }
    }



}
