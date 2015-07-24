package io.dropwizard.metrics;

/**
 * An interface for metric types which have counts.
 */
public interface Counting extends Metric {

	MetricAttribute<? super Counting, Long> COUNT = new AbstractMetricAttribute<Counting, Long>("count") {
		@Override
		public Long getValue(Counting metric) {
			return metric.getCount();
		}
	};
	
	/**
     * Returns the current count.
     *
     * @return the current count
     */
    long getCount();
}
