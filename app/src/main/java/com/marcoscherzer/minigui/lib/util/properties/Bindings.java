package com.marcoscherzer.minigui.lib.util.properties;

import java.util.concurrent.Callable;

public class Bindings {

    public static SimpleStringProperty createStringBinding(Callable<String> callable, ObservableValue<?>... dependencies) {
        String initialValue;
        try {
            initialValue = callable.call();
        } catch (Exception e) {
            initialValue = "";
        }

        SimpleStringProperty boundProperty = new SimpleStringProperty(initialValue);

        ChangeListener<Object> listener = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<Object> observable, Object oldValue, Object newValue) {
                try {
                    boundProperty.set(callable.call());
                } catch (Exception e) {
                    // Optional: log or ignore
                }
            }
        };

        for (ObservableValue<?> dep : dependencies) {
            dep.addListener((ChangeListener) listener); // unchecked but safe
        }

        return boundProperty;
    }
}



