package io.dropwizard.metrics;

import org.junit.Test;

import io.dropwizard.metrics.Histogram;
import io.dropwizard.metrics.Reservoir;
import io.dropwizard.metrics.Snapshot;

import static org.assertj.core.api.Assertions.assertThat;
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

      assertThat(Sampling.MAX.getValue(hist))
        .isEqualTo(hist.getSnapshot().getMax());
      assertThat(Sampling.MEAN.getValue(hist))
        .isEqualTo(hist.getSnapshot().getMean());
      assertThat(Sampling.MIN.getValue(hist))
        .isEqualTo(hist.getSnapshot().getMin());
      assertThat(Sampling.PERCENTILE_75.getValue(hist))
        .isEqualTo(hist.getSnapshot().get75thPercentile());
      assertThat(Sampling.PERCENTILE_95.getValue(hist))
        .isEqualTo(hist.getSnapshot().get95thPercentile());
      assertThat(Sampling.PERCENTILE_98.getValue(hist))
        .isEqualTo(hist.getSnapshot().get98thPercentile());
      assertThat(Sampling.PERCENTILE_99.getValue(hist))
        .isEqualTo(hist.getSnapshot().get99thPercentile());
      assertThat(Sampling.PERCENTILE_999.getValue(hist))
        .isEqualTo(hist.getSnapshot().get999thPercentile());
    }
}
