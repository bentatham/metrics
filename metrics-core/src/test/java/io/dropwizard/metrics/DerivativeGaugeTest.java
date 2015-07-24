package io.dropwizard.metrics;

import org.junit.Test;

import io.dropwizard.metrics.DerivativeGauge;
import io.dropwizard.metrics.Gauge;
import io.dropwizard.metrics.Gauge.GaugeAttribute;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Set;

public class DerivativeGaugeTest {
    private final Gauge<String> gauge1 = new Gauge<String>() {
        @Override
        public String getValue() {
            return "woo";
        }
        
        public Set<MetricAttribute<? extends Metric,?>> getAttributes() {
        	return Collections.<MetricAttribute<? extends Metric,?>> singleton(new GaugeAttribute<String>("string"));
        }
    };
    private final Gauge<Integer> gauge2 = new DerivativeGauge<String, Integer>(gauge1) {
        @Override
        protected Integer transform(String value) {
            return value.length();
        }
        
        public Set<MetricAttribute<? extends Metric,?>> getAttributes() {
        	return Collections.<MetricAttribute<? extends Metric,?>> singleton(new GaugeAttribute<Integer>("integer"));
        }
    };

    @Test
    public void returnsATransformedValue() throws Exception {
        assertThat(gauge2.getValue())
                .isEqualTo(3);
    }
}
