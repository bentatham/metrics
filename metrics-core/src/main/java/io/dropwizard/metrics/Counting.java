package io.dropwizard.metrics;

/**
 * An interface for metric types which have counts.
 */
public interface Counting {
    /**
     * Returns the current count.
     *
     * @return the current count
     */
    long getCount();
    
    enum Field implements MetricField {
      COUNT;
      
      @Override
      public Object get(Object counting) {
        return ((Counting) counting).getCount();
      }
    }
}
