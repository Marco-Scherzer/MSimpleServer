package com.marcoscherzer.minigui.lib.util.properties;

import java.util.ArrayList;
import java.util.List;

public class SimpleStringProperty implements ObservableValue<String> {
    private final List<ChangeListener<String>> listeners = new ArrayList<>();
    private String value;
    private ObservableValue<String> boundTo = null;

    public SimpleStringProperty() {
        this("");
    }

    public SimpleStringProperty(String initialValue) {
        this.value = initialValue;
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public void set(String newValue) {
        if (!value.equals(newValue)) {
            String old = value;
            value = newValue;
            notifyListeners(old, newValue);
        }
    }

    @Override
    public void addListener(ChangeListener<String> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<String> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(String oldValue, String newValue) {
        for (ChangeListener<String> listener : listeners) {
            listener.changed(this, oldValue, newValue);
        }
    }

    @Override
    public void bind(ObservableValue<String> other) {
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

