package io.dropwizard.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A set of gauges for the JVM name, vendor, and uptime.
 */
public class JvmAttributeGaugeSet extends AbstractMetricSet {
    private final RuntimeMXBean runtime;

    /**
     * Creates a new set of gauges.
     */
    public JvmAttributeGaugeSet() {
        this(ManagementFactory.getRuntimeMXBean());
    }

    /**
     * Creates a new set of gauges with the given {@link RuntimeMXBean}.
     * @param runtime JVM management interface with access to system properties
     */
    public JvmAttributeGaugeSet(RuntimeMXBean runtime) {
        this.runtime = runtime;
    }

    @Override
    public Map<MetricName, Metric> getMetrics() {
        final Map<MetricName, Metric> gauges = new HashMap<MetricName, Metric>();

        gauges.put(MetricName.build("name"), new Gauge<String>() {
            @Override
            public String getValue() {
                return runtime.getName();
            }
            
            @Override
            public Set<MetricAttribute<? extends Metric, ?>> getAttributes() {
            	return Collections.<MetricAttribute<? extends Metric, ?>> singleton(
            			new GaugeAttribute<Object>("name"));
            }
        });

        gauges.put(MetricName.build("vendor"), new Gauge<String>() {
            @Override
            public String getValue() {
                return String.format(Locale.US,
                                     "%s %s %s (%s)",
                                     runtime.getVmVendor(),
                                     runtime.getVmName(),
                                     runtime.getVmVersion(),
                                     runtime.getSpecVersion());
            }
            
            @Override
            public Set<MetricAttribute<? extends Metric, ?>> getAttributes() {
            	return Collections.<MetricAttribute<? extends Metric, ?>> singleton(
            			new GaugeAttribute<Object>("vendor"));
            }
        });

        gauges.put(MetricName.build("uptime"), new Gauge<Long>() {
            @Override
            public Long getValue() {
                return runtime.getUptime();
            }
            
            @Override
            public Set<MetricAttribute<? extends Metric, ?>> getAttributes() {
            	return Collections.<MetricAttribute<? extends Metric, ?>> singleton(
            			new GaugeAttribute<Object>("uptime"));
            }
        });

        return Collections.unmodifiableMap(gauges);
    }
}
