package io.dropwizard.metrics;

import org.junit.Before;
import org.junit.Test;

import io.dropwizard.metrics.Clock;
import io.dropwizard.metrics.Meter;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeterTest {
    private final Clock clock = mock(Clock.class);
    private final Meter meter = new Meter(clock);

    @Before
    public void setUp() throws Exception {
        when(clock.getTick()).thenReturn(0L, TimeUnit.SECONDS.toNanos(10));

    }

    @Test
    public void startsOutWithNoRatesOrCount() throws Exception {
        assertThat(meter.getCount())
                .isZero();

        assertThat(meter.getMeanRate())
                .isEqualTo(0.0, offset(0.001));

        assertThat(meter.getOneMinuteRate())
                .isEqualTo(0.0, offset(0.001));

        assertThat(meter.getFiveMinuteRate())
                .isEqualTo(0.0, offset(0.001));

        assertThat(meter.getFifteenMinuteRate())
                .isEqualTo(0.0, offset(0.001));
    }

    @Test
    public void marksEventsAndUpdatesRatesAndCount() throws Exception {
        meter.mark();
        meter.mark(2);

        assertThat(meter.getMeanRate())
                .isEqualTo(0.3, offset(0.001));

        assertThat(meter.getOneMinuteRate())
                .isEqualTo(0.1840, offset(0.001));

        assertThat(meter.getFiveMinuteRate())
                .isEqualTo(0.1966, offset(0.001));

        assertThat(meter.getFifteenMinuteRate())
                .isEqualTo(0.1988, offset(0.001));
    }

    @Test
    public void testMetricField() {
      meter.mark();
      meter.mark(2);
      assertThat(meter.getField(Metered.Field.RATE_15M))
        .isEqualTo(meter.getFifteenMinuteRate());
      assertThat(meter.getField(Metered.Field.RATE_5M))
        .isEqualTo(meter.getFiveMinuteRate());
      assertThat(meter.getField(Metered.Field.RATE_1M))
        .isEqualTo(meter.getOneMinuteRate());
      assertThat(meter.getField(Metered.Field.RATE_MEAN))
        .isEqualTo(meter.getMeanRate());
      assertThat(meter.getField(Counting.Field.COUNT))
        .isEqualTo(meter.getCount());

      try {
        meter.getField(Sampling.Field.MAX);
        fail("expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e) {
        // expected
      }
    }
}
