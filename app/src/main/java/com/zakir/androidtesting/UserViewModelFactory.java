package com.zakir.androidtesting;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    UserViewModel userViewModel;

    @Inject
    public UserViewModelFactory(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) userViewModel;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
