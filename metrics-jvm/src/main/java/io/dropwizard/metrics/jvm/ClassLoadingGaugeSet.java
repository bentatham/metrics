package io.dropwizard.metrics.jvm;

import io.dropwizard.metrics.AbstractGauge;
import io.dropwizard.metrics.AbstractMetricSet;
import io.dropwizard.metrics.Metric;
import io.dropwizard.metrics.MetricName;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * A set of gauges for JVM classloader usage.
 */
public class ClassLoadingGaugeSet extends AbstractMetricSet {

    private final ClassLoadingMXBean mxBean;

    public ClassLoadingGaugeSet() {
        this(ManagementFactory.getClassLoadingMXBean());
    }

    public ClassLoadingGaugeSet(ClassLoadingMXBean mxBean) {
        this.mxBean = mxBean;
    }

    @Override
    public Map<MetricName, Metric> getMetrics() {
        final Map<MetricName, Metric> gauges = new HashMap<MetricName, Metric>();

        gauges.put(MetricName.build("loaded"), new AbstractGauge<Long>() {
            @Override
            public Long getValue() {
                return mxBean.getTotalLoadedClassCount();
            }
        });

        gauges.put(MetricName.build("unloaded"), new AbstractGauge<Long>() {
            @Override
            public Long getValue() {
                return mxBean.getUnloadedClassCount();
            }
        });

        return gauges;
    }
}
