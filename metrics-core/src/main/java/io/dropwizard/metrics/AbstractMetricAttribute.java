package io.dropwizard.metrics;

public abstract class AbstractMetricAttribute<M extends Metric, V> implements MetricAttribute<M, V> {
	private String label;
	private MetricAttribute.ValueType valueType;

	public AbstractMetricAttribute(String label) {
		this(label, MetricAttribute.ValueType.RAW);
	}
	
	public AbstractMetricAttribute(String label, MetricAttribute.ValueType valueType) {
		super();
		this.label = label;
		this.valueType = valueType;
	}
	
	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public MetricAttribute.ValueType getValueType() {
		return valueType;
	}
}