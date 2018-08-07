package com.zakir.androidtesting;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zakir.androidtesting.di.LocalUserRepository;
import com.zakir.androidtesting.di.ObserverScheduler;
import com.zakir.androidtesting.di.SubscribeScheduler;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;

import org.apache.commons.collections.map.HashedMap;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class UserViewModel extends ViewModel {

    UserRepository userRepository;
    MutableLiveData<Response<List<User>>> responseMutableLiveData = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Scheduler subscribeSchedular, observeSchedular;
    private MutableLiveData<Response<User>> userMutableLiveData = new MutableLiveData<>();
    private Map<Long, User> userMap = new HashedMap();

    @Inject
    public UserViewModel(@LocalUserRepository UserRepository userRepository,
                         @SubscribeScheduler Scheduler subscribeScheduler,
                         @ObserverScheduler Scheduler observeScheduler) {
        this.userRepository = userRepository;
        this.subscribeSchedular = subscribeScheduler;
        this.observeSchedular = observeScheduler;
    }

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
                            Observable.fromIterable(users)
                                    .map(user -> userMap.put(user.getId(), user));
                        },
                        exp -> responseMutableLiveData.setValue(Response.error(exp))
                ));
    }

    public void loadUser(long userId) {
        if (userMap.containsKey(userId)) {
            userMutableLiveData.setValue(Response.success(userMap.get(userId)));
            return;
        }
        compositeDisposable.add(userRepository.getUserById(userId)
        .subscribeOn(subscribeSchedular)
        .observeOn(observeSchedular)
        .doOnSubscribe(d -> userMutableLiveData.postValue(Response.loading()))
        .subscribe(
                user -> {
                    userMutableLiveData.setValue(Response.success(user));
                    userMap.put(user.getId(), user);
                },
                throwable -> userMutableLiveData.setValue(Response.error(throwable)),
                () -> compositeDisposable.dispose()
        ));
    }

    public MutableLiveData<Response<List<User>>> getUsersMutableLiveData() {
        return responseMutableLiveData;
    }

    public void dispose() {
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    public MutableLiveData<Response<User>> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
