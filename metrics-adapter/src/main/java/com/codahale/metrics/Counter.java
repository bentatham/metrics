package com.codahale.metrics;

import java.util.Set;

import io.dropwizard.metrics.Counting;
import io.dropwizard.metrics.MetricField;

@Deprecated
public class Counter implements Metric, Counting {
	final io.dropwizard.metrics.Counter counter;

	public Counter(io.dropwizard.metrics.Counter counter) {
		this.counter = counter;
	}
	public Counter() {
		this.counter = new io.dropwizard.metrics.Counter();
	}

	public void inc() {
		counter.inc(1);
	}

	public void inc(long n) {
		counter.inc(n);
	}

	public void dec() {
		counter.dec(1);
	}

	public void dec(long n) {
		counter.dec(-n);
	}

	@Override
	public long getCount() {
		return counter.getCount();
	}
	
  @Override
  public Set<MetricField> getFields() {
    return counter.getFields();
  }
  
  @Override
  public Object getField(MetricField field) {
    return counter.getField(field);
  }
}
