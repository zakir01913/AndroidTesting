package com.zakir.androidtesting.addUser;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.di.LocalUserRepository;
import com.zakir.androidtesting.di.ObserverScheduler;
import com.zakir.androidtesting.di.SubscribeScheduler;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;

import org.apache.commons.validator.routines.EmailValidator;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zakir on 26/7/18.
 */

public class AddUserViewModel extends ViewModel {
    UserRepository userRepository;
    private User user;
    private MutableLiveData<Response<User>> response = new MutableLiveData<>();
    private Scheduler subscribeScheduler;
    private Scheduler observerScheduler;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public AddUserViewModel(
            @LocalUserRepository UserRepository userRepository,
            @SubscribeScheduler Scheduler subscribeScheduler,
            @ObserverScheduler Scheduler observerScheduler
    ) {
        this.userRepository = userRepository;
        this.subscribeScheduler = subscribeScheduler;
        this.observerScheduler = observerScheduler;
    }

    public void insert(User user) {
        if (validate(user)) {
            this.user = user;
            compositeDisposable.add(Observable.fromCallable(() -> {
                long id = userRepository.insert(user);
                return id;
            })
            .doOnSubscribe( disposable -> response.postValue(Response.loading()))
            .subscribeOn(subscribeScheduler)
            .observeOn(observerScheduler)
            .subscribe(id -> {
                System.out.println(id);
                user.setId(id);
                response.setValue(Response.success(user));
            }));
        }
    }

    public MutableLiveData<Response<User>> response() {
        return response;
    }

    private boolean validate(User user) {
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            response.setValue(Response.error(new AddUserException(
                    "First name can't be empty", AddUserException.ErrorCode.EMPTY_FIRST_NAME)));
            return false;
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            response.setValue(Response.error(new AddUserException(
                    "Last name can't be empty", AddUserException.ErrorCode.EMPTY_LAST_NAME)));
            return false;
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || !EmailValidator.getInstance().isValid(user.getEmail())) {
            response.setValue(Response.error(new AddUserException(
                    "Invalid email address", AddUserException.ErrorCode.INVALID_EMAIL)));
            return false;
        }

        return true;
    }

    public void dispose() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
