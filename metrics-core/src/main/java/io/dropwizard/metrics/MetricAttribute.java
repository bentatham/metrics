package io.dropwizard.metrics;

public interface MetricAttribute<M extends Metric, V> {
    
	public enum ValueType {
		RAW, RATE, DURATION;
	}
	
	String getLabel();
    
  V getValue(M metric);
    
  ValueType getValueType();
}
