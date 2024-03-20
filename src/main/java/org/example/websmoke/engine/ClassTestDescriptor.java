package org.example.websmoke.engine;

import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN;

class ClassTestDescriptor extends AbstractTestDescriptor {

	private final Class<?> testClass;

	public ClassTestDescriptor(Class<?> testClass, TestDescriptor parent) {
		super( //
				parent.getUniqueId().append("class", testClass.getName()), //
				testClass.getSimpleName(), //
				ClassSource.from(testClass) //
		);
		this.testClass = testClass;
		setParent(parent);
		addAllChildren();
	}


	private void addAllChildren() {

		Predicate<Field> isTestField = field -> AnnotationUtils.isAnnotated(field, GET.class);

		ReflectionUtils
                .findFields(testClass, isTestField, TOP_DOWN)
                .stream() //
				.map(field -> new FieldTestDescriptor(field, this)) //
				.forEach(this::addChild);
	}

	@Override
	public Type getType() {
		return Type.CONTAINER;
	}
}
