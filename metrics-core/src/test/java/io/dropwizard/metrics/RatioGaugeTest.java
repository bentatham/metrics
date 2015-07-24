package io.dropwizard.metrics;

import org.junit.Test;

import io.dropwizard.metrics.RatioGauge;
import io.dropwizard.metrics.Gauge.GaugeAttribute;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Set;

public class RatioGaugeTest {
    @Test
    public void ratiosAreHumanReadable() throws Exception {
        final RatioGauge.Ratio ratio = RatioGauge.Ratio.of(100, 200);

        assertThat(ratio.toString())
                .isEqualTo("100.0:200.0");
    }

    @Test
    public void calculatesTheRatioOfTheNumeratorToTheDenominator() throws Exception {
        final RatioGauge regular = new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return RatioGauge.Ratio.of(2, 4);
            }
            
            public Set<MetricAttribute<? extends Metric,?>> getAttributes() {
            	return Collections.<MetricAttribute<? extends Metric,?>> singleton(new GaugeAttribute<Double>("ratio"));
            }
        };

        assertThat(regular.getValue())
                .isEqualTo(0.5);
    }

    @Test
    public void handlesDivideByZeroIssues() throws Exception {
        final RatioGauge divByZero = new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(100, 0);
            }
            
            public Set<MetricAttribute<? extends Metric,?>> getAttributes() {
            	return Collections.<MetricAttribute<? extends Metric,?>> singleton(new GaugeAttribute<Double>("ratio"));
            }
        };

        assertThat(divByZero.getValue())
                .isNaN();
    }

    @Test
    public void handlesInfiniteDenominators() throws Exception {
        final RatioGauge infinite = new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(10, Double.POSITIVE_INFINITY);
            }
            
            public Set<MetricAttribute<? extends Metric,?>> getAttributes() {
            	return Collections.<MetricAttribute<? extends Metric,?>> singleton(new GaugeAttribute<Double>("ratio"));
            }
        };

        assertThat(infinite.getValue())
                .isNaN();
    }

    @Test
    public void handlesNaNDenominators() throws Exception {
        final RatioGauge nan = new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(10, Double.NaN);
            }
            
            public Set<MetricAttribute<? extends Metric,?>> getAttributes() {
            	return Collections.<MetricAttribute<? extends Metric,?>> singleton(new GaugeAttribute<Double>("ratio"));
            }
        };

        assertThat(nan.getValue())
                .isNaN();
    }
}
