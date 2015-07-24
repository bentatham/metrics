package io.dropwizard.metrics;

import java.util.Set;

/**
 * A tag interface to indicate that a class is a metric.
 */
public interface Metric {

	Set<MetricAttribute<? extends Metric, ?>> getAttributes();

}
