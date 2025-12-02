package com.marcoscherzer.minigui.lib.util.properties;

import java.util.ArrayList;
import java.util.List;

public class SimpleDoubleProperty implements ObservableValue<Double> {
    private final List<ChangeListener<Double>> listeners = new ArrayList<>();
    private double value;
    private ObservableValue<Double> boundTo = null;

    public SimpleDoubleProperty() {
        this(0.0);
    }

    public SimpleDoubleProperty(double initialValue) {
        this.value = initialValue;
    }

    @Override
    public Double get() {
        return value;
    }

    @Override
    public void set(Double newValue) {
        if (Double.compare(value, newValue) != 0) {
            double old = value;
            value = newValue;
            notifyListeners(old, newValue);
        }
    }

    @Override
    public void addListener(ChangeListener<Double> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<Double> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(double oldValue, double newValue) {
        for (ChangeListener<Double> listener : listeners) {
            listener.changed(this, oldValue, newValue);
        }
    }

    @Override
    public void bind(ObservableValue<Double> other) {
        if (boundTo != null) unbind();
        boundTo = other;
        boundTo.addListener((obs, oldVal, newVal) -> set(newVal));
        set(boundTo.get());
    }

    @Override
    public void unbind() {
        if (boundTo != null) {
            boundTo.removeListener((obs, oldVal, newVal) -> set(newVal));
            boundTo = null;
        }
    }
}
