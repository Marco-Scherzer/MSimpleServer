package com.marcoscherzer.minigui.lib.util.properties;

public interface ChangeListener<T> {
    void changed(ObservableValue<T> observable, T oldValue, T newValue);
}
