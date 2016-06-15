package com.codahale.metrics;

import java.util.Set;

import io.dropwizard.metrics.MetricField;

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
	public Object getField(MetricField field)	{
	  return gauge.getField(field);
	}
	
	@Override
	public Set<MetricField> getFields()	{
	  return gauge.getFields();
	}
}
