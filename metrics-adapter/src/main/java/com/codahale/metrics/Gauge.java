package com.codahale.metrics;

import java.util.Set;

import io.dropwizard.metrics.MetricAttribute;

@Deprecated
public class Gauge<T> implements io.dropwizard.metrics.Gauge<T>, Metric {
	final io.dropwizard.metrics.Gauge<T> gauge;
	
	public Gauge(io.dropwizard.metrics.Gauge<T> gauge){
		this.gauge = gauge;
	}

	@Override
	public T getValue() {
		return gauge.getValue();
	}
	
	 @Override
	  public Set<MetricAttribute< ? extends io.dropwizard.metrics.Metric, ? >> getAttributes()  {
	   return gauge.getAttributes();
	  }
}
