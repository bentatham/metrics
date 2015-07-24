package io.dropwizard.metrics;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import io.dropwizard.metrics.MetricAttribute.ValueType;

/**
 * A metric which calculates the distribution of a value.
 *
 * @see <a href="http://www.johndcook.com/standard_deviation.html">Accurately computing running
 *      variance</a>
 */
public class Histogram implements Metric, Sampling, Counting {
							
	private static final Map<ValueType, Set<MetricAttribute<? extends Metric, ?>>> ATTRIBUTES = Collections.unmodifiableMap(
			new HashMap<ValueType, Set<MetricAttribute<? extends Metric, ?>>>() {
			  {
			    put(ValueType.RAW, Collections.unmodifiableSet(new LinkedHashSet<MetricAttribute<? extends Metric, ?>>() {
		        {
		          add(Counting.COUNT);
		          add(Sampling.MAX);
		          add(Sampling.MEAN);
		          add(Sampling.MIN);
		          add(Sampling.STDDEV);
		          add(Sampling.PERCENTILE_50);
		          add(Sampling.PERCENTILE_75);
		          add(Sampling.PERCENTILE_95);
		          add(Sampling.PERCENTILE_98);
		          add(Sampling.PERCENTILE_99);
		          add(Sampling.PERCENTILE_999);
		        }
			    }));
			    put(ValueType.DURATION, Collections.unmodifiableSet(new LinkedHashSet<MetricAttribute<? extends Metric, ?>>() {
			      {
  			        for (final MetricAttribute attribute : get(ValueType.RAW)) {
      	            add(new MetricAttribute() {
        	              @Override
        	              public String getLabel() {
        	                return attribute.getLabel();
        	              }
        
        	              @Override
        	              public Object getValue(Metric metric) {
        	                return attribute.getValue(metric);
        	              }
        
        	              @Override
        	              public ValueType getValueType() {
        	                return ValueType.DURATION;
        	              }
      	            });
  			        }
			      }
			    }));
			  }
			});
	
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

	@Override
    public Set<MetricAttribute<? extends Metric, ?>> getAttributes() {
    	  return getAttributes(ValueType.RAW);
    }
	
	  public Set<MetricAttribute<? extends Metric, ?>> getAttributes(final ValueType valueType) {
	      return ATTRIBUTES.get(valueType);
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
}
