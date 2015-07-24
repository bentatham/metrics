package io.dropwizard.metrics;

/**
 * An object which samples values.
 */
public interface Sampling extends Metric {
	MetricAttribute<Sampling, Double> PERCENTILE_50 = new AbstractMetricAttribute<Sampling, Double>("p50") {
		@Override
		public Double getValue(Sampling metric) {
			return metric.getSnapshot().getMedian();
		}
	};
	MetricAttribute<Sampling, Double> PERCENTILE_75 = new AbstractMetricAttribute<Sampling, Double>("p75"){
		@Override
		public Double getValue(Sampling metric) {
			return metric.getSnapshot().get75thPercentile();
		}
	};
	MetricAttribute<Sampling, Double> PERCENTILE_95 = new AbstractMetricAttribute<Sampling, Double>("p95"){
		@Override
		public Double getValue(Sampling metric) {
			return metric.getSnapshot().get95thPercentile();
		}
	};
	MetricAttribute<Sampling, Double> PERCENTILE_98 = new AbstractMetricAttribute<Sampling, Double>("p98"){
		@Override
		public Double getValue(Sampling metric) {
			return metric.getSnapshot().get98thPercentile();
		}

	};
	MetricAttribute<Sampling, Double> PERCENTILE_99 = new AbstractMetricAttribute<Sampling, Double>("p99"){
		@Override
		public Double getValue(Sampling metric) {
			return metric.getSnapshot().get99thPercentile();
		}

	};
	MetricAttribute<Sampling, Double> PERCENTILE_999 = new AbstractMetricAttribute<Sampling, Double>("p999"){
		@Override
		public Double getValue(Sampling metric) {
			return metric.getSnapshot().get999thPercentile();
		}

	};
	MetricAttribute<Sampling, Long> MAX = new AbstractMetricAttribute<Sampling, Long>("max"){
		@Override
		public Long getValue(Sampling metric) {
			return metric.getSnapshot().getMax();
		}

	};
	MetricAttribute<Sampling, Double> MEAN = new AbstractMetricAttribute<Sampling, Double>("mean") {
		@Override
		public Double getValue(Sampling metric) {
			return metric.getSnapshot().getMean();
		}

	};
	MetricAttribute<Sampling, Long> MIN = new AbstractMetricAttribute<Sampling, Long>("min"){
			public Long getValue(Sampling metric) {
				return metric.getSnapshot().getMin();
			}
	};
	MetricAttribute<Sampling, Double> STDDEV = new AbstractMetricAttribute<Sampling, Double>("stddev") {
		public Double getValue(Sampling metric) {
	return metric.getSnapshot().getStdDev();
		}
	};
	
	/**
     * Returns a snapshot of the values.
     *
     * @return a snapshot of the values
     */
    Snapshot getSnapshot();
}
