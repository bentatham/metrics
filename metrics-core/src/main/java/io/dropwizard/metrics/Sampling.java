package io.dropwizard.metrics;

/**
 * An object which samples values.
 */
public interface Sampling {
    /**
     * Returns a snapshot of the values.
     *
     * @return a snapshot of the values
     */
    Snapshot getSnapshot();
    
    enum Field implements MetricField {
      MEDIAN(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().getMedian();
        } 
      }),
      MEAN(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().getMean();
        } 
      }),
      MAX(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().getMax();
        } 
      }),
      MIN(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().getMin();
        } 
      }),
      STDDEV(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().getStdDev();
        } 
      }),
      P75(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().get75thPercentile();
        } 
      }),
      P95(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().get95thPercentile();
        } 
      }),
      P98(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().get98thPercentile();
        } 
      }),
      P99(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().get99thPercentile();
        } 
      }),
      P999(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Sampling) metric).getSnapshot().get999thPercentile();
        } 
      });
      
      private MetricField m_provider;
      
      Field(MetricField provider) {
        m_provider = provider;
      }
      
      @Override
      public Object get(Object sampling) {
        return m_provider.get(sampling);
      }
    }
}
