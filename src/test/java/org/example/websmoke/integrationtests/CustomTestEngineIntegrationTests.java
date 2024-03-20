package org.example.websmoke.integrationtests;

import org.example.websmoke.tests.ExampleWebSmokeTests;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;


class CustomTestEngineIntegrationTests {

    // tag::for_article[]

    @Test
    void ourCustomTestEngineFiresCorrectLifecycleEvents() {
      EngineTestKit
                .engine("websmoke-test")
                .selectors(selectClass(ExampleWebSmokeTests.class))
                .execute()
                .testEvents()
                .assertStatistics(stats -> stats
                        .started(3)      // the example test class has 3 test cases
                        .succeeded(2)    // 2 successes
                        .failed(1)       // and 1 failure
                );
    }

    // end::for_article[]

}