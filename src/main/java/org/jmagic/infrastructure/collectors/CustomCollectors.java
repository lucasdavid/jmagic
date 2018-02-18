package org.jmagic.infrastructure.collectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author ldavid
 */
public abstract class CustomCollectors {

    public static <T, A extends List<T>> Collector<T, A, List<T>> toImmutableList(Supplier<A> collectionFactory) {
        return Collector.of(collectionFactory, List::add, (left, right) -> {
            left.addAll(right);
            return left;
        }, Collections::unmodifiableList);
    }

    public static <t> Collector<t, List<t>, List<t>> toImmutableList() {
        return toImmutableList(ArrayList::new);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collector<T, ?, List<T>> toShuffledList() {
        return toShuffledList(null);
    }

    public static <T> Collector<T, ?, List<T>> toShuffledList(Random random) {
        return Collectors.collectingAndThen(
            Collectors.toList(),
            list -> {
                if (random == null)
                    Collections.shuffle(list);
                else
                    Collections.shuffle(list, random);

                return list;
            }
        );
    }
}
