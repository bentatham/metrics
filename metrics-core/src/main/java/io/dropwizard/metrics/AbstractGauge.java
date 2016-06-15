package io.dropwizard.metrics;

import java.util.Collections;
import java.util.Set;

/**
 * This class can be removed in favor of <code>default</code> methods on the interface, if we move to java8.
 * 
 * @author bentatham
 */
public abstract class AbstractGauge<T> implements Gauge<T> {
  @Override
  public Set<MetricField> getFields() {
    return Collections.<MetricField> singleton(Gauge.Field.VALUE);
  }
  
  @Override
  public Object getField(MetricField field) {
    if (field == Gauge.Field.VALUE) {
      return getValue();
    }
    throw new IllegalArgumentException(field.toString());
  }
}
