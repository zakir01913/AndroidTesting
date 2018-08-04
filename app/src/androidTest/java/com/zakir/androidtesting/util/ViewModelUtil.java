package com.zakir.androidtesting.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ViewModelUtil {

    public static <T extends ViewModel> ViewModelProvider.Factory createFactory(T model) {

        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.isAssignableFrom(model.getClass())) {
                    return (T) model;
                }
                throw new IllegalArgumentException("Unexpected model class " + modelClass);
            }
        };

    }
}
