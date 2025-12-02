package com.marcoscherzer.minigui.lib.util.properties;

import java.util.ArrayList;
import java.util.List;

public class SimpleLongProperty implements ObservableValue<Long> {
    private final List<ChangeListener<Long>> listeners = new ArrayList<>();
    private long value;
    private ObservableValue<Long> boundTo = null;

    public SimpleLongProperty() {
        this(0L);
    }

    public SimpleLongProperty(long initialValue) {
        this.value = initialValue;
    }

    @Override
    public Long get() {
        return value;
    }

    @Override
    public void set(Long newValue) {
        if (value != newValue) {
            long old = value;
            value = newValue;
            notifyListeners(old, newValue);
        }
    }

    @Override
    public void addListener(ChangeListener<Long> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<Long> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(long oldValue, long newValue) {
        for (ChangeListener<Long> listener : listeners) {
            listener.changed(this, oldValue, newValue);
        }
    }

    @Override
    public void bind(ObservableValue<Long> other) {
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
