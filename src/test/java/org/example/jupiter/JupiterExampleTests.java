package org.example.jupiter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JupiterExampleTests {

    @Test
    void successful() {
        assertTrue(true);
    }

    @Test
    void failing() {
        fail();
    }
}
