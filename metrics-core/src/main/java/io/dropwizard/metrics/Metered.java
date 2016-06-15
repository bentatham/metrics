package io.dropwizard.metrics;

/**
 * An object which maintains mean and exponentially-weighted rate.
 */
public interface Metered extends Metric, Counting {
    /**
     * Returns the number of events which have been marked.
     *
     * @return the number of events which have been marked
     */
    @Override
    long getCount();

    /**
     * Returns the fifteen-minute exponentially-weighted moving average rate at which events have
     * occurred since the meter was created.
     * <p/>
     * This rate has the same exponential decay factor as the fifteen-minute load average in the
     * {@code top} Unix command.
     *
     * @return the fifteen-minute exponentially-weighted moving average rate at which events have
     *         occurred since the meter was created
     */
    double getFifteenMinuteRate();

    /**
     * Returns the five-minute exponentially-weighted moving average rate at which events have
     * occurred since the meter was created.
     * <p/>
     * This rate has the same exponential decay factor as the five-minute load average in the {@code
     * top} Unix command.
     *
     * @return the five-minute exponentially-weighted moving average rate at which events have
     *         occurred since the meter was created
     */
    double getFiveMinuteRate();

    /**
     * Returns the mean rate at which events have occurred since the meter was created.
     *
     * @return the mean rate at which events have occurred since the meter was created
     */
    double getMeanRate();

    /**
     * Returns the one-minute exponentially-weighted moving average rate at which events have
     * occurred since the meter was created.
     * <p/>
     * This rate has the same exponential decay factor as the one-minute load average in the {@code
     * top} Unix command.
     *
     * @return the one-minute exponentially-weighted moving average rate at which events have
     *         occurred since the meter was created
     */
    double getOneMinuteRate();
    
    enum Field implements MetricField {
      RATE_15M(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Metered) metric).getFifteenMinuteRate();
        } 
      }), 
      RATE_5M(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Metered) metric).getFiveMinuteRate();
        } 
      }),
      RATE_1M(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Metered) metric).getOneMinuteRate();
        } 
      }),
      RATE_MEAN(new MetricField() {
        @Override
        public Object get(Object metric) {
          return ((Metered) metric).getMeanRate();
        } 
      });
      
      private MetricField m_provider;
      
      Field(MetricField provider)
      {
        m_provider = provider;
      }
      
      @Override
      public Object get(Object metric) {
        return m_provider.get(metric);
      }
    }
}
