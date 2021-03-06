package io.dropwizard.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CsvReporterTest {
    @Rule public final TemporaryFolder folder = new TemporaryFolder();

    private final MetricRegistry registry = mock(MetricRegistry.class);
    private final Clock clock = mock(Clock.class);

    private File dataDirectory;
    private CsvReporter reporter;
    private Map<String,String> testTags;

    @Before
    public void setUp() throws Exception {
        when(clock.getTime()).thenReturn(19910191000L);

        this.dataDirectory = folder.newFolder();

        this.reporter = CsvReporter.forRegistry(registry)
                                   .formatFor(Locale.US)
                                   .convertRatesTo(TimeUnit.SECONDS)
                                   .convertDurationsTo(TimeUnit.MILLISECONDS)
                                   .withClock(clock)
                                   .filter(MetricFilter.ALL)
                                   .build(dataDirectory);
        this.testTags = new HashMap<>();
        this.testTags.put("t1", "tv1");
        this.testTags.put("k2", "k3");
        
    }

    @Test
    public void reportsGaugeValues() throws Exception {
        final Gauge gauge = mock(Gauge.class);
        when(gauge.getValue()).thenReturn(1);

        reporter.report(map("gauge", testTags, gauge),
                        this.<Counter>map(),
                        this.<Histogram>map(),
                        this.<Meter>map(),
                        this.<Timer>map());

        assertThat(fileContents("gauge.k2.k3.t1.tv1.csv"))
                .isEqualTo(csv(
                        "t,value",
                        "19910191,1"
                ));
    }

    @Test
    public void reportsCounterValues() throws Exception {
        final Counter counter = mock(Counter.class);
        when(counter.getCount()).thenReturn(100L);

        reporter.report(this.<Gauge>map(),
                        map("test.counter", testTags, counter),
                        this.<Histogram>map(),
                        this.<Meter>map(),
                        this.<Timer>map());

        assertThat(fileContents("test.counter.k2.k3.t1.tv1.csv"))
                .isEqualTo(csv(
                        "t,count",
                        "19910191,100"
                ));
    }

    @Test
    public void reportsHistogramValues() throws Exception {
        final Histogram histogram = mock(Histogram.class);
        when(histogram.getCount()).thenReturn(1L);

        final Snapshot snapshot = mock(Snapshot.class);
        when(snapshot.getMax()).thenReturn(2L);
        when(snapshot.getMean()).thenReturn(3.0);
        when(snapshot.getMin()).thenReturn(4L);
        when(snapshot.getStdDev()).thenReturn(5.0);
        when(snapshot.getMedian()).thenReturn(6.0);
        when(snapshot.get75thPercentile()).thenReturn(7.0);
        when(snapshot.get95thPercentile()).thenReturn(8.0);
        when(snapshot.get98thPercentile()).thenReturn(9.0);
        when(snapshot.get99thPercentile()).thenReturn(10.0);
        when(snapshot.get999thPercentile()).thenReturn(11.0);

        when(histogram.getSnapshot()).thenReturn(snapshot);

        reporter.report(this.<Gauge>map(),
                        this.<Counter>map(),
                        map("test.histogram", testTags, histogram),
                        this.<Meter>map(),
                        this.<Timer>map());

        assertThat(fileContents("test.histogram.k2.k3.t1.tv1.csv"))
                .isEqualTo(csv(
                        "t,count,max,mean,min,stddev,p50,p75,p95,p98,p99,p999",
                        "19910191,1,2,3.000000,4,5.000000,6.000000,7.000000,8.000000,9.000000,10.000000,11.000000"
                ));
    }

    @Test
    public void reportsMeterValues() throws Exception {
        final Meter meter = mock(Meter.class);
        when(meter.getCount()).thenReturn(1L);
        when(meter.getMeanRate()).thenReturn(2.0);
        when(meter.getOneMinuteRate()).thenReturn(3.0);
        when(meter.getFiveMinuteRate()).thenReturn(4.0);
        when(meter.getFifteenMinuteRate()).thenReturn(5.0);

        reporter.report(this.<Gauge>map(),
                        this.<Counter>map(),
                        this.<Histogram>map(),
                        map("test.meter",testTags, meter),
                        this.<Timer>map());

        assertThat(fileContents("test.meter.k2.k3.t1.tv1.csv"))
                .isEqualTo(csv(
                        "t,count,mean_rate,m1_rate,m5_rate,m15_rate,rate_unit",
                        "19910191,1,2.000000,3.000000,4.000000,5.000000,events/second"
                ));
    }

    @Test
    public void reportsTimerValues() throws Exception {
        final Timer timer = mock(Timer.class);
        when(timer.getCount()).thenReturn(1L);
        when(timer.getMeanRate()).thenReturn(2.0);
        when(timer.getOneMinuteRate()).thenReturn(3.0);
        when(timer.getFiveMinuteRate()).thenReturn(4.0);
        when(timer.getFifteenMinuteRate()).thenReturn(5.0);

        final Snapshot snapshot = mock(Snapshot.class);
        when(snapshot.getMax()).thenReturn(TimeUnit.MILLISECONDS.toNanos(100));
        when(snapshot.getMean()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(200));
        when(snapshot.getMin()).thenReturn(TimeUnit.MILLISECONDS.toNanos(300));
        when(snapshot.getStdDev()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(400));
        when(snapshot.getMedian()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(500));
        when(snapshot.get75thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(600));
        when(snapshot.get95thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(700));
        when(snapshot.get98thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(800));
        when(snapshot.get99thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(900));
        when(snapshot.get999thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(1000));

        when(timer.getSnapshot()).thenReturn(snapshot);

        reporter.report(this.<Gauge>map(),
                        this.<Counter>map(),
                        this.<Histogram>map(),
                        this.<Meter>map(),
                        map("test.another.timer", testTags, timer));

        assertThat(fileContents("test.another.timer.k2.k3.t1.tv1.csv"))
                .isEqualTo(csv(
                        "t,count,max,mean,min,stddev,p50,p75,p95,p98,p99,p999,mean_rate,m1_rate,m5_rate,m15_rate,rate_unit,duration_unit",
                        "19910191,1,100.000000,200.000000,300.000000,400.000000,500.000000,600.000000,700.000000,800.000000,900.000000,1000.000000,2.000000,3.000000,4.000000,5.000000,calls/second,milliseconds"
                ));
    }

    @Test
    public void testCsvFileProviderIsUsed() {
        CsvFileProvider fileProvider = mock(CsvFileProvider.class);
        when(fileProvider.getFile(dataDirectory, new MetricName("gauge",testTags))).thenReturn(new File(dataDirectory, "guage.csv"));

        CsvReporter reporter = CsvReporter.forRegistry(registry)
                .withCsvFileProvider(fileProvider)
                .build(dataDirectory);

        final Gauge gauge = mock(Gauge.class);
        when(gauge.getValue()).thenReturn(1);

        reporter.report(map("gauge", testTags, gauge),
                this.<Counter>map(),
                this.<Histogram>map(),
                this.<Meter>map(),
                this.<Timer>map());

        verify(fileProvider).getFile(dataDirectory, new MetricName("gauge",testTags));
    }

    private String csv(String... lines) {
        final StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append(String.format("%n"));
        }
        return builder.toString();
    }

    private String fileContents(String filename) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final FileInputStream input = new FileInputStream(new File(dataDirectory, filename));
        try {
            final InputStreamReader reader = new InputStreamReader(input);
            final BufferedReader bufferedReader = new BufferedReader(reader);
            final CharBuffer buf = CharBuffer.allocate(1024);
            while (bufferedReader.read(buf) != -1) {
                buf.flip();
                builder.append(buf);
                buf.clear();
            }
        } finally {
            input.close();
        }
        return builder.toString();
    }

    private <T> SortedMap<MetricName, T> map() {
        return new TreeMap<>();
    }

    private <T> SortedMap<MetricName, T> map(String name, Map<String,String> tags, T metric) {
        final TreeMap<MetricName, T> map = new TreeMap<>();
        map.put(new MetricName(name,tags), metric);
        return map;
    }
}
