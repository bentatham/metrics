package io.dropwizard.metrics;

import static org.junit.Assert.fail;

import org.junit.Test;

import io.dropwizard.metrics.Counter;

import static org.assertj.core.api.Assertions.assertThat;

public class CounterTest {
    private final Counter counter = new Counter();

    @Test
    public void startsAtZero() throws Exception {
        assertThat(counter.getCount())
                .isZero();
    }

    @Test
    public void incrementsByOne() throws Exception {
        counter.inc();

        assertThat(counter.getCount())
                .isEqualTo(1);
    }

    @Test
    public void incrementsByAnArbitraryDelta() throws Exception {
        counter.inc(12);

        assertThat(counter.getCount())
                .isEqualTo(12);
    }

    @Test
    public void decrementsByOne() throws Exception {
        counter.dec();

        assertThat(counter.getCount())
                .isEqualTo(-1);
    }

    @Test
    public void decrementsByAnArbitraryDelta() throws Exception {
        counter.dec(12);

        assertThat(counter.getCount())
                .isEqualTo(-12);
    }

    @Test
    public void testMetricField() {
      assertThat(counter.getField(Counting.Field.COUNT))
        .isEqualTo(counter.getCount());
      counter.inc(20);
      assertThat(counter.getField(Counting.Field.COUNT))
        .isEqualTo(counter.getCount());

      try {
        counter.getField(Sampling.Field.MAX);
        fail("expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e) {
        // expected
      }
    }
}
