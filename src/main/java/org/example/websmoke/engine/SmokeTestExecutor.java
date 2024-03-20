package org.example.websmoke.engine;

import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static org.example.websmoke.engine.FieldTestDescriptor.getExpectedStatusCode;
import static org.example.websmoke.engine.FieldTestDescriptor.getUrl;


public class SmokeTestExecutor {

    public void execute(ExecutionRequest request, TestDescriptor descriptor) {
        if (descriptor instanceof EngineDescriptor) {
            executeContainer(request, descriptor);
        }

        if (descriptor instanceof ClassTestDescriptor) {
            executeContainer(request, descriptor);
        }

        if (descriptor instanceof FieldTestDescriptor) {
            executeTest(request, (FieldTestDescriptor) descriptor);
        }
    }

    // tag::for_article[]

    private void executeTest(ExecutionRequest request, FieldTestDescriptor fieldTestDescriptor) {
        request.getEngineExecutionListener().executionStarted(fieldTestDescriptor);
        TestExecutionResult executionResult = executeTestField(fieldTestDescriptor);
        request.getEngineExecutionListener().executionFinished(fieldTestDescriptor, executionResult);
    }

    private TestExecutionResult executeTestField(FieldTestDescriptor descriptor) {

        Field testField = descriptor.getTestField();

        try {
            int expected = getExpectedStatusCode(testField);
            String url = getUrl(testField);

            HttpResponse<String> response = this.executeHttpRequest(url);
            int actual = response.statusCode();
            if (expected != actual) {
                var message = String.format("expected HTTP status code %d but received %d from server", expected, actual);
                return TestExecutionResult.failed(new AssertionFailedError(message, expected, actual));
            }

        } catch (Exception e) {
            return TestExecutionResult.failed(new RuntimeException("Failed to execute HTTP request", e));
        }

        return TestExecutionResult.successful();
    }

    // end::for_article[]



    private HttpResponse<String> executeHttpRequest(String url) throws Exception {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        return HttpClient
                .newBuilder()
                .build()
                .send(request, BodyHandlers.ofString());
    }


    private void executeContainer(ExecutionRequest request, TestDescriptor containerDescriptor) {
        request.getEngineExecutionListener().executionStarted(containerDescriptor);
        containerDescriptor.getChildren()
                .forEach(descriptor -> execute(request, descriptor));
        request.getEngineExecutionListener().executionFinished(containerDescriptor, TestExecutionResult.successful());
    }
}
