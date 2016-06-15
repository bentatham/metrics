package io.dropwizard.metrics.jvm;

import static io.dropwizard.metrics.MetricRegistry.name;

import io.dropwizard.metrics.AbstractGauge;
import io.dropwizard.metrics.AbstractMetricSet;
import io.dropwizard.metrics.Metric;
import io.dropwizard.metrics.MetricName;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A set of gauges for the number of threads in their various states and deadlock detection.
 */
public class ThreadStatesGaugeSet extends AbstractMetricSet {

    // do not compute stack traces.
    private final static int STACK_TRACE_DEPTH = 0;

    private final ThreadMXBean threads;
    private final ThreadDeadlockDetector deadlockDetector;

    /**
     * Creates a new set of gauges using the default MXBeans.
     */
    public ThreadStatesGaugeSet() {
        this(ManagementFactory.getThreadMXBean(), new ThreadDeadlockDetector());
    }

    /**
     * Creates a new set of gauges using the given MXBean and detector.
     *
     * @param threads          a thread MXBean
     * @param deadlockDetector a deadlock detector
     */
    public ThreadStatesGaugeSet(ThreadMXBean threads,
                                ThreadDeadlockDetector deadlockDetector) {
        this.threads = threads;
        this.deadlockDetector = deadlockDetector;
    }

    @Override
    public Map<MetricName, Metric> getMetrics() {
        final Map<MetricName, Metric> gauges = new HashMap<MetricName, Metric>();

        for (final Thread.State state : Thread.State.values()) {
            gauges.put(name(state.toString().toLowerCase(), "count"),
                       new AbstractGauge<Integer>() {
                           @Override
                           public Integer getValue() {
                               return getThreadCount(state);
                           }
                       });
        }

        gauges.put(MetricName.build("count"), new AbstractGauge<Integer>() {
            @Override
            public Integer getValue() {
                return threads.getThreadCount();
            }
        });

        gauges.put(MetricName.build("daemon.count"), new AbstractGauge<Integer>() {
            @Override
            public Integer getValue() {
                return threads.getDaemonThreadCount();
            }
        });

        gauges.put(MetricName.build("deadlock.count"), new AbstractGauge<Integer>() {
            @Override
            public Integer getValue() {
                return deadlockDetector.getDeadlockedThreads().size();
            }
        });

        gauges.put(MetricName.build("deadlocks"), new AbstractGauge<Set<String>>() {
            @Override
            public Set<String> getValue() {
                return deadlockDetector.getDeadlockedThreads();
            }
        });

        return Collections.unmodifiableMap(gauges);
    }

    private int getThreadCount(Thread.State state) {
        final ThreadInfo[] allThreads = getThreadInfo();
        int count = 0;
        for (ThreadInfo info : allThreads) {
            if (info != null && info.getThreadState() == state) {
                count++;
            }
        }
        return count;
    }

    ThreadInfo[] getThreadInfo() {
        return threads.getThreadInfo(threads.getAllThreadIds(), STACK_TRACE_DEPTH);
    }

}
