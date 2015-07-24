package io.dropwizard.metrics;

import io.dropwizard.metrics.MetricAttribute.ValueType;

/**
 * An object which maintains mean and exponentially-weighted rate.
 */
public interface Metered extends Metric, Counting {

	public static final MetricAttribute<Metered, Double> RATE_FIFTEEN_MINUTE = new AbstractMetricAttribute<Metered, Double>("m15_rate", ValueType.RATE) {
		public Double getValue(Metered metric) { 
			return metric.getFifteenMinuteRate();
		}
	};
	public static final MetricAttribute<Metered, Double> RATE_FIVE_MINUTE = new AbstractMetricAttribute<Metered, Double>("m5_rate", ValueType.RATE) {
		public Double getValue(Metered metric) { 
			return metric.getFiveMinuteRate();
		}
	};
	public static final MetricAttribute<Metered, Double> RATE_ONE_MINUTE = new AbstractMetricAttribute<Metered, Double>("m1_rate", ValueType.RATE) {
		public Double getValue(Metered metric) { 
			return metric.getOneMinuteRate();
		}
	};
	public static final MetricAttribute<Metered, Double> RATE_MEAN = new AbstractMetricAttribute<Metered, Double>("mean_rate", ValueType.RATE) {
		public Double getValue(Metered metric) { 
			return metric.getMeanRate();
		}
	};
	
	
	/**
     * Returns the number of events which have been marked.
     *
     * @return the number of events which have been marked
     */
    @Override
    long getCount();

    /**
     * Returns the fifteen-minute exponentially-weighted moving average rate at which events have
     * occurred since the meter was created.
     * <p/>
     * This rate has the same exponential decay factor as the fifteen-minute load average in the
     * {@code top} Unix command.
     *
     * @return the fifteen-minute exponentially-weighted moving average rate at which events have
     *         occurred since the meter was created
     */
    double getFifteenMinuteRate();

    /**
     * Returns the five-minute exponentially-weighted moving average rate at which events have
     * occurred since the meter was created.
     * <p/>
     * This rate has the same exponential decay factor as the five-minute load average in the {@code
     * top} Unix command.
     *
     * @return the five-minute exponentially-weighted moving average rate at which events have
     *         occurred since the meter was created
     */
    double getFiveMinuteRate();

    /**
     * Returns the mean rate at which events have occurred since the meter was created.
     *
     * @return the mean rate at which events have occurred since the meter was created
     */
    double getMeanRate();

    /**
     * Returns the one-minute exponentially-weighted moving average rate at which events have
     * occurred since the meter was created.
     * <p/>
     * This rate has the same exponential decay factor as the one-minute load average in the {@code
     * top} Unix command.
     *
     * @return the one-minute exponentially-weighted moving average rate at which events have
     *         occurred since the meter was created
     */
    double getOneMinuteRate();
}
