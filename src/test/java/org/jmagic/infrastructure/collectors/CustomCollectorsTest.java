package org.jmagic.infrastructure.collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CustomCollectorsTest.
 *
 * @author ldavid
 */
class CustomCollectorsTest {


    private List<String> originalList;

    @BeforeEach
    void setUp() {
        originalList = new ArrayList<>(
            List.of("first", "second", "third"));
    }

    @Test
    void toImmutableList() {
        for (List<String> immutableList : List.of(
            originalList.stream().collect(CustomCollectors.toImmutableList()),
            originalList.stream().collect(CustomCollectors.toImmutableList(Vector::new)))) {

            assertEquals(3, immutableList.size());
            assertThrows(UnsupportedOperationException.class, () -> immutableList.remove(0));
            assertThrows(UnsupportedOperationException.class, () -> immutableList.add("forth"));
        }
    }

    @Test
    void toShuffledList() {
        List<String> shuffled = originalList.stream().collect(CustomCollectors.toShuffledList());
        assertNotNull(shuffled);
        assertEquals(shuffled.size(), originalList.size());
    }

    @Test
    void toShuffledList1() {
        Random r = new Random(42);
        List<String> shuffled = originalList.stream().collect(CustomCollectors.toShuffledList(r));
        assertNotNull(shuffled);
        assertEquals(shuffled.size(), originalList.size());
    }

}