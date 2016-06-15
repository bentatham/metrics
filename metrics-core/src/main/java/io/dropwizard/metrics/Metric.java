package io.dropwizard.metrics;

import java.util.Set;

/**
 * A tag interface to indicate that a class is a metric.
 */
public interface Metric {

  /**
   * @param field
   * @since 4.0
   * @return the value of the given field
   * @throws IllegalArgumentException if the given field is not available on this metric
   */
  Object getField(MetricField field);
  
  /**
   * @Since 4.0
   * @return the set of available fields
   */
  Set<MetricField> getFields();
  
}
