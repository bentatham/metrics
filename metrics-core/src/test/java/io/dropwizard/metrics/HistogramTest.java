package io.dropwizard.metrics;

import org.junit.Test;

import io.dropwizard.metrics.Histogram;
import io.dropwizard.metrics.Reservoir;
import io.dropwizard.metrics.Snapshot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class HistogramTest {
    private final Reservoir reservoir = mock(Reservoir.class);
    private final Histogram histogram = new Histogram(reservoir);

    @Test
    public void updatesTheCountOnUpdates() throws Exception {
        assertThat(histogram.getCount())
                .isZero();

        histogram.update(1);

        assertThat(histogram.getCount())
                .isEqualTo(1);
    }

    @Test
    public void returnsTheSnapshotFromTheReservoir() throws Exception {
        final Snapshot snapshot = mock(Snapshot.class);
        when(reservoir.getSnapshot()).thenReturn(snapshot);

        assertThat(histogram.getSnapshot())
                .isEqualTo(snapshot);
    }

    @Test
    public void updatesTheReservoir() throws Exception {
        histogram.update(1);

        verify(reservoir).update(1);
    }

    @Test
    public void testMetricField() {
      Histogram hist = new Histogram(new UniformReservoir());
      hist.update(1);

      assertThat(hist.getField(Sampling.Field.MAX))
        .isEqualTo(hist.getSnapshot().getMax());
      assertThat(hist.getField(Sampling.Field.MEAN))
        .isEqualTo(hist.getSnapshot().getMean());
      assertThat(hist.getField(Sampling.Field.MEDIAN))
        .isEqualTo(hist.getSnapshot().getMedian());
      assertThat(hist.getField(Sampling.Field.MIN))
        .isEqualTo(hist.getSnapshot().getMin());
      assertThat(hist.getField(Sampling.Field.P75))
        .isEqualTo(hist.getSnapshot().get75thPercentile());
      assertThat(hist.getField(Sampling.Field.P95))
        .isEqualTo(hist.getSnapshot().get95thPercentile());
      assertThat(hist.getField(Sampling.Field.P98))
        .isEqualTo(hist.getSnapshot().get98thPercentile());
      assertThat(hist.getField(Sampling.Field.P99))
        .isEqualTo(hist.getSnapshot().get99thPercentile());
      assertThat(hist.getField(Sampling.Field.P999))
        .isEqualTo(hist.getSnapshot().get999thPercentile());

      try {
        hist.getField(Gauge.Field.VALUE);
        fail("expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e) {
        // expected
      }
    }
}
