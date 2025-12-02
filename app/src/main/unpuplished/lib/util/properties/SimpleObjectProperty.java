package com.marcoscherzer.minigui.lib.util.properties;

import java.util.ArrayList;
import java.util.List;

public class SimpleObjectProperty<T> implements ObservableValue<T> {
    private final List<ChangeListener<T>> listeners = new ArrayList<>();
    private T value;
    private ObservableValue<T> boundTo = null;

    public SimpleObjectProperty() {
        this(null);
    }

    public SimpleObjectProperty(T initialValue) {
        this.value = initialValue;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T newValue) {
        if ((value == null && newValue != null) || (value != null && !value.equals(newValue))) {
            T old = value;
            value = newValue;
            notifyListeners(old, newValue);
        }
    }

    @Override
    public void addListener(ChangeListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<T> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(T oldValue, T newValue) {
        for (ChangeListener<T> listener : listeners) {
            listener.changed(this, oldValue, newValue);
        }
    }

    @Override
    public void bind(ObservableValue<T> other) {
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

