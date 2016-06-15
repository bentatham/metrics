package io.dropwizard.metrics;

import java.util.Set;

/**
 * This class can be removed in favor of default methods on <code>MetricSet</code> if we move to java8. 
 * @author BenTatham
 */
public abstract class AbstractMetricSet implements MetricSet {
  @Override
  public Set<MetricField> getFields() {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public Object getField(MetricField field) {
    throw new UnsupportedOperationException();
  }
}
