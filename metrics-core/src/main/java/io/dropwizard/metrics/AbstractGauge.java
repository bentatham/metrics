package io.dropwizard.metrics;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractGauge<T> implements Gauge<T> {
	@Override
	public Set<MetricAttribute<? extends Metric, ?>> getAttributes() {
		return Collections
				.<MetricAttribute<? extends Metric, ?>> singleton(new AbstractMetricAttribute<Gauge<T>, T>("") {
					@Override
					public T getValue(Gauge<T> metric) {
						return metric.getValue();
					}
				});
	}
}
