package com.zakir.androidtesting.addUser;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zakir.androidtesting.UserViewModel;

import javax.inject.Inject;

public class AddUserViewModelFactory implements ViewModelProvider.Factory {

    private AddUserViewModel addUserViewModel;

    @Inject
    public AddUserViewModelFactory(AddUserViewModel addUserViewModel) {
        this.addUserViewModel = addUserViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddUserViewModel.class)) {
            return (T) addUserViewModel;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
