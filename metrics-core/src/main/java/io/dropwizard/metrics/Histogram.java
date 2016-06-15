package io.dropwizard.metrics;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A metric which calculates the distribution of a value.
 *
 * @see <a href="http://www.johndcook.com/standard_deviation.html">Accurately computing running
 *      variance</a>
 */
public class Histogram implements Metric, Sampling, Counting {
  public static final Set<MetricField> FIELDS = Collections.unmodifiableSet(new HashSet<MetricField>() {{
      addAll(Arrays.asList(Counting.Field.values()));
      addAll(Arrays.asList(Sampling.Field.values()));
   }});
  
  private final Reservoir reservoir;
    private final LongAdder count;

    /**
     * Creates a new {@link Histogram} with the given reservoir.
     *
     * @param reservoir the reservoir to create a histogram from
     */
    public Histogram(Reservoir reservoir) {
        this.reservoir = reservoir;
        this.count = LongAdderFactory.create();
    }

    /**
     * Adds a recorded value.
     *
     * @param value the length of the value
     */
    public void update(int value) {
        update((long) value);
    }

    /**
     * Adds a recorded value.
     *
     * @param value the length of the value
     */
    public void update(long value) {
        count.increment();
        reservoir.update(value);
    }

    /**
     * Returns the number of values recorded.
     *
     * @return the number of values recorded
     */
    @Override
    public long getCount() {
        return count.sum();
    }

    @Override
    public Snapshot getSnapshot() {
        return reservoir.getSnapshot();
    }

    @Override
    public Set<MetricField> getFields() {
      return FIELDS;
    }
    
    @Override
    public Object getField(MetricField field) {
      if (getFields().contains(field)) {
        return field.get(this);
      }
      throw new IllegalArgumentException(String.valueOf(field));
    }
}
