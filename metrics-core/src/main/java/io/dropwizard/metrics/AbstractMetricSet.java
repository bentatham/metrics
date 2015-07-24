package io.dropwizard.metrics;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractMetricSet implements MetricSet {

	@Override
	public Set<MetricAttribute<? extends Metric, ?>> getAttributes() {
		Set<MetricAttribute<? extends Metric, ?>> result = new HashSet<>();
		for (Metric metric : getMetrics().values()) {
			result.addAll(metric.getAttributes());
		}
		return result;
	}

}
