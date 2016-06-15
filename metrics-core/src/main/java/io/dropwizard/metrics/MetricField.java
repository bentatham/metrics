package io.dropwizard.metrics;

/**
 * A representation of a field (aka attribute, measurement) of a metric. Useful for filtering parts of a Metric in
 * reporters, amonng other things.
 * 
 * @author bentatham
 */
public interface MetricField {
  Object get(Object metric);
}
