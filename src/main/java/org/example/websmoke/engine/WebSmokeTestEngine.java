package org.example.websmoke.engine;

import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.discovery.PackageSelector;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.net.URI;
import java.util.function.Predicate;


// tag::for_article[]

public class WebSmokeTestEngine implements TestEngine {

    private static final Predicate<Class<?>> IS_WEBSMOKE_TEST_CONTAINER
            = classCandidate -> AnnotationSupport.isAnnotated(classCandidate, WebSmokeTest.class);


    @Override
    public String getId() {
        return "websmoke-test";
    }


    @Override
    public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
        TestDescriptor engineDescriptor = new EngineDescriptor(uniqueId, "Web Smoke Test");

        request.getSelectorsByType(ClasspathRootSelector.class).forEach(selector -> {
            appendTestsInClasspathRoot(selector.getClasspathRoot(), engineDescriptor);
        });

        request.getSelectorsByType(PackageSelector.class).forEach(selector -> {
            appendTestsInPackage(selector.getPackageName(), engineDescriptor);
        });

        request.getSelectorsByType(ClassSelector.class).forEach(selector -> {
            appendTestsInClass(selector.getJavaClass(), engineDescriptor);
        });

        return engineDescriptor;
    }

    private void appendTestsInClasspathRoot(URI uri, TestDescriptor engineDescriptor) {
        ReflectionSupport.findAllClassesInClasspathRoot(uri, IS_WEBSMOKE_TEST_CONTAINER, name -> true) //
                .stream() //
                .map(aClass -> new ClassTestDescriptor(aClass, engineDescriptor)) //
                .forEach(engineDescriptor::addChild);
    }


    private void appendTestsInPackage(String packageName, TestDescriptor engineDescriptor) {
        ReflectionSupport.findAllClassesInPackage(packageName, IS_WEBSMOKE_TEST_CONTAINER, name -> true) //
                .stream() //
                .map(aClass -> new ClassTestDescriptor(aClass, engineDescriptor)) //
                .forEach(engineDescriptor::addChild);
    }


    private void appendTestsInClass(Class<?> javaClass, TestDescriptor engineDescriptor) {
        if (AnnotationSupport.isAnnotated(javaClass, WebSmokeTest.class)) {
            engineDescriptor.addChild(new ClassTestDescriptor(javaClass, engineDescriptor));
        }
    }


    @Override
    public void execute(ExecutionRequest request) {
        TestDescriptor root = request.getRootTestDescriptor();

        new SmokeTestExecutor().execute(request, root);
    }




}
// end::for_article[]
