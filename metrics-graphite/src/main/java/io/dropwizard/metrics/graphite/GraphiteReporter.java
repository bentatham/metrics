package io.dropwizard.metrics.graphite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.metrics.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * A reporter which publishes metric values to a Graphite server.
 *
 * @see <a href="http://graphite.wikidot.com/">Graphite - Scalable Realtime Graphing</a>
 */
public class GraphiteReporter extends ScheduledReporter {
    /**
     * Returns a new {@link Builder} for {@link GraphiteReporter}.
     *
     * @param registry the registry to report
     * @return a {@link Builder} instance for a {@link GraphiteReporter}
     */
    public static Builder forRegistry(MetricRegistry registry) {
        return new Builder(registry);
    }

    /**
     * A builder for {@link GraphiteReporter} instances. Defaults to not using a prefix, using the
     * default clock, converting rates to events/second, converting durations to milliseconds, and
     * not filtering metrics.
     */
    public static class Builder {
        private final MetricRegistry registry;
        private Clock clock;
        private String prefix;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;

        private Builder(MetricRegistry registry) {
            this.registry = registry;
            this.clock = Clock.defaultClock();
            this.prefix = null;
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
        }

        /**
         * Use the given {@link Clock} instance for the time.
         *
         * @param clock a {@link Clock} instance
         * @return {@code this}
         */
        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        /**
         * Prefix all metric names with the given string.
         *
         * @param prefix the prefix for all metric names
         * @return {@code this}
         */
        public Builder prefixedWith(String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Convert rates to the given time unit.
         *
         * @param rateUnit a unit of time
         * @return {@code this}
         */
        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        /**
         * Convert durations to the given time unit.
         *
         * @param durationUnit a unit of time
         * @return {@code this}
         */
        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        /**
         * Only report metrics which match the given filter.
         *
         * @param filter a {@link MetricFilter}
         * @return {@code this}
         */
        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Builds a {@link GraphiteReporter} with the given properties, sending metrics using the
         * given {@link GraphiteSender}.
         *
         * @param graphite a {@link GraphiteSender}
         * @return a {@link GraphiteReporter}
         */
        public GraphiteReporter build(GraphiteSender graphite) {
            return new GraphiteReporter(registry,
                                        graphite,
                                        clock,
                                        prefix,
                                        rateUnit,
                                        durationUnit,
                                        filter);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphiteReporter.class);

    private final GraphiteSender graphite;
    private final Clock clock;
    private final MetricName prefix;

    private GraphiteReporter(MetricRegistry registry,
                             GraphiteSender graphite,
                             Clock clock,
                             String prefix,
                             TimeUnit rateUnit,
                             TimeUnit durationUnit,
                             MetricFilter filter) {
        super(registry, "graphite-reporter", filter, rateUnit, durationUnit);
        this.graphite = graphite;
        this.clock = clock;
        this.prefix = MetricName.build(prefix);
    }

    @Override
    public void report(SortedMap<MetricName, Gauge> gauges,
                       SortedMap<MetricName, Counter> counters,
                       SortedMap<MetricName, Histogram> histograms,
                       SortedMap<MetricName, Meter> meters,
                       SortedMap<MetricName, Timer> timers) {
        final long timestamp = clock.getTime() / 1000;

        // oh it'd be lovely to use Java 7 here
        try {
            if (!graphite.isConnected()) {
    	          graphite.connect();
            }

            reportMetrics(gauges, timestamp);
            reportMetrics(counters, timestamp);
            reportMetrics(histograms, timestamp);
            reportMetrics(meters, timestamp);
            reportMetrics(timers, timestamp);
            
            graphite.flush();
        } catch (Throwable t) {
            LOGGER.warn("Unable to report to Graphite", graphite, t);
            closeGraphiteConnection();
        }
    }

    private void closeGraphiteConnection() {
        try {
            graphite.close();
        } catch (IOException e) {
            LOGGER.warn("Unable to report to Graphite", graphite, e);
            try {
                graphite.close();
            } catch (IOException e1) {
                LOGGER.warn("Error closing Graphite", graphite, e1);
            }
        }
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } finally {
            try {
                graphite.close();
            } catch (IOException e) {
                LOGGER.debug("Error disconnecting from Graphite", graphite, e);
            }
        }
    }

    private <M extends Metric> void reportMetrics(Map<MetricName, M> metrics, long timestamp) throws IOException {
      for (Map.Entry<MetricName, M> entry : metrics.entrySet()) {
        reportMetric(entry.getKey(), entry.getValue(), timestamp);
      }
    }
    
    private void reportMetric(MetricName name, Metric metric, long timestamp) throws IOException {
        for (MetricAttribute attribute : metric.getAttributes()) {
          	Object value = attribute.getValue(metric);
          	String string;
          	switch (attribute.getValueType()) {
          	case DURATION : 
          		string = format(value.getClass().equals(Double.class) ? convertDuration((Double) value) : convertDuration((Long) value));
          		break;
          	case RATE : 
          		string = format(convertRate((Double) value));
          		break;
          	case RAW: 
          	default:
          		string = format(value);
          		break;
          	}
          	String label = attribute.getLabel();
  			    String prefixedName = (label == null || label.isEmpty()) ? prefix(name) : prefix(name, label);
  			    if (string != null) {
  			        graphite.send(prefixedName, string, timestamp);
  			    }
        }
    }

    private String format(Object o) {
        if (o instanceof Float) {
            return format(((Float) o).doubleValue());
        } else if (o instanceof Double) {
            return format(((Double) o).doubleValue());
        } else if (o instanceof Byte) {
            return format(((Byte) o).longValue());
        } else if (o instanceof Short) {
            return format(((Short) o).longValue());
        } else if (o instanceof Integer) {
            return format(((Integer) o).longValue());
        } else if (o instanceof Long) {
            return format(((Long) o).longValue());
        } else if (o instanceof Boolean) {
          return ((Boolean) o) ? "1" : "0";
        }        
        return null;
    }

    private String prefix(MetricName name, String... components) {
        return MetricName.join(MetricName.join(prefix, name), MetricName.build(components)).getKey();
    }

    private String format(long n) {
        return Long.toString(n);
    }

    private String format(double v) {
        // the Carbon plaintext format is pretty underspecified, but it seems like it just wants
        // US-formatted digits
        return String.format(Locale.US, "%2.2f", v);
    }
}
