package org.jmagic;

import org.junit.jupiter.api.Test;

import java.util.Collections;

class RunnerTest {

    @Test
    void testSanityMain() {
        Runner.main(null);
    }

    @Test
    void testRunStress() {
        new Runner(
                1, 2, 20,
                1000, Collections.emptyList(), 42).run();

        new Runner(
                1, 2, 80,
                1000, Collections.emptyList(), 42).run();

        new Runner(
                5, 2, 20,
                1000, Collections.emptyList(), 42).run();

//        // TODO: check why games with more than 2 players are crashing.
//        new Runner(
//                1, 3, 20,
//                1000, Collections.emptyList(), 42).run();

//        new Runner(
//                1, 4, 20,
//                1000, Collections.emptyList(), 42).run();
    }
}
