package com.marcoscherzer.minigui.lib.util.properties;

public interface ObservableValue<T> {
    T get();

    void set(T value);

    void addListener(ChangeListener<T> listener);

    void removeListener(ChangeListener<T> listener);

    void bind(ObservableValue<T> other);

    void unbind();
}


