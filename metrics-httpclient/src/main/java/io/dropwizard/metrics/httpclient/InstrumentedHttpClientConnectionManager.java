package io.dropwizard.metrics.httpclient;

import static io.dropwizard.metrics.MetricRegistry.name;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;

import io.dropwizard.metrics.AbstractGauge;
import io.dropwizard.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * A {@link HttpClientConnectionManager} which monitors the number of open connections.
 */
public class InstrumentedHttpClientConnectionManager extends PoolingHttpClientConnectionManager {


    protected static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
    }

    private final MetricRegistry metricsRegistry;
    private final String name;

    public InstrumentedHttpClientConnectionManager(MetricRegistry metricRegistry) {
        this(metricRegistry, getDefaultRegistry());
    }

    public InstrumentedHttpClientConnectionManager(MetricRegistry metricsRegistry,
                                                   Registry<ConnectionSocketFactory> socketFactoryRegistry) {
        this(metricsRegistry, socketFactoryRegistry, -1, TimeUnit.MILLISECONDS);
    }


    public InstrumentedHttpClientConnectionManager(MetricRegistry metricsRegistry,
                                                   Registry<ConnectionSocketFactory> socketFactoryRegistry,
                                                   long connTTL,
                                                   TimeUnit connTTLTimeUnit) {
        this(metricsRegistry, socketFactoryRegistry, null, null, SystemDefaultDnsResolver.INSTANCE, connTTL, connTTLTimeUnit, null);
    }

    public InstrumentedHttpClientConnectionManager(MetricRegistry metricsRegistry,
                                                   Registry<ConnectionSocketFactory> socketFactoryRegistry,
                                                   HttpConnectionFactory<HttpRoute,ManagedHttpClientConnection> connFactory,
                                                   SchemePortResolver schemePortResolver,
                                                   DnsResolver dnsResolver,
                                                   long connTTL,
                                                   TimeUnit connTTLTimeUnit,
                                                   String name) {
        super(socketFactoryRegistry, connFactory, schemePortResolver, dnsResolver, connTTL, connTTLTimeUnit);
        this.metricsRegistry = metricsRegistry;
        this.name = name;
        metricsRegistry.register(name(HttpClientConnectionManager.class, name, "available-connections"),
                                 new AbstractGauge<Integer>() {
                                     @Override
                                     public Integer getValue() {
                                         // this acquires a lock on the connection pool; remove if contention sucks
                                         return getTotalStats().getAvailable();
                                     }
                                 });
        metricsRegistry.register(name(HttpClientConnectionManager.class, name, "leased-connections"),
                                 new AbstractGauge<Integer>() {
                                     @Override
                                     public Integer getValue() {
                                         // this acquires a lock on the connection pool; remove if contention sucks
                                         return getTotalStats().getLeased();
                                     }
                                 });
        metricsRegistry.register(name(HttpClientConnectionManager.class, name, "max-connections"),
                                 new AbstractGauge<Integer>() {
                                     @Override
                                     public Integer getValue() {
                                         // this acquires a lock on the connection pool; remove if contention sucks
                                         return getTotalStats().getMax();
                                     }
                                 });
        metricsRegistry.register(name(HttpClientConnectionManager.class, name, "pending-connections"),
                                 new AbstractGauge<Integer>() {
                                     @Override
                                     public Integer getValue() {
                                         // this acquires a lock on the connection pool; remove if contention sucks
                                         return getTotalStats().getPending();
                                     }
                                 });
    }

    @Override
    public void shutdown() {
        super.shutdown();
        metricsRegistry.remove(name(HttpClientConnectionManager.class, name, "available-connections"));
        metricsRegistry.remove(name(HttpClientConnectionManager.class, name, "leased-connections"));
        metricsRegistry.remove(name(HttpClientConnectionManager.class, name, "max-connections"));
        metricsRegistry.remove(name(HttpClientConnectionManager.class, name, "pending-connections"));
    }
}
