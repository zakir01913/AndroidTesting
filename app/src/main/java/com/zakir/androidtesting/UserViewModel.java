package com.zakir.androidtesting;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class UserViewModel extends ViewModel {

    @Inject
    UserRepository userRepository;
    MutableLiveData<Response<List<User>>> responseMutableLiveData = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Scheduler subscribeSchedular, observeSchedular;

    public void loadUsers() {
        compositeDisposable.add(userRepository.getUsers()
                .subscribeOn(subscribeSchedular)
                .observeOn(observeSchedular)
                .doOnSubscribe(d -> {
                    responseMutableLiveData.postValue(Response.loading());
                })
                .subscribe(
                        users -> {
                            responseMutableLiveData.setValue(Response.success(users));
                        },
                        exp -> responseMutableLiveData.setValue(Response.error(exp))
                ));
    }

    public MutableLiveData<Response<List<User>>> getResponseMutableLiveData() {
        return responseMutableLiveData;
    }

    public void dispose() {
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }
}
