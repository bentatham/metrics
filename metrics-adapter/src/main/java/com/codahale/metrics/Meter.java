package com.codahale.metrics;

import java.util.Set;

import io.dropwizard.metrics.Metered;
import io.dropwizard.metrics.MetricAttribute;

@Deprecated
public class Meter implements Metered, Metric {
	final io.dropwizard.metrics.Meter meter;

	public Meter(io.dropwizard.metrics.Meter meter) {
		this.meter = meter;
	}

	public void mark() {
		meter.mark();
	}

	public void mark(long n) {
		meter.mark(n);
	}

	@Override
	public long getCount() {
		return meter.getCount();
	}

	@Override
	public double getFifteenMinuteRate() {
		return meter.getFifteenMinuteRate();
	}

	@Override
	public double getFiveMinuteRate() {
		return meter.getFiveMinuteRate();
	}

	@Override
	public double getMeanRate() {
		return meter.getMeanRate();
	}

	@Override
	public double getOneMinuteRate() {
		return meter.getOneMinuteRate();
	}
	
  @Override
  public Set<MetricAttribute< ? extends io.dropwizard.metrics.Metric, ? >> getAttributes()  {
    return meter.getAttributes();
  }
}
