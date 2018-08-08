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
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class UserViewModel extends ViewModel {

    UserRepository userRepository;
    MutableLiveData<Response<List<User>>> userListResponseMutableLiveData = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Scheduler subscribeScheduler, observeScheduler;
    private MutableLiveData<Response<User>> userMutableLiveData = new MutableLiveData<>();
    private Map<Long, User> userMap = new HashedMap();
    private Disposable deleteDisposable;

    @Inject
    public UserViewModel(@LocalUserRepository UserRepository userRepository,
                         @SubscribeScheduler Scheduler subscribeScheduler,
                         @ObserverScheduler Scheduler observeScheduler) {
        this.userRepository = userRepository;
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
    }

    public void loadUsers() {
        compositeDisposable.add(userRepository.getUsers()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .doOnSubscribe(d -> {
                    userListResponseMutableLiveData.postValue(Response.loading());
                })
                .subscribe(
                        users -> {
                            userListResponseMutableLiveData.setValue(Response.success(users));
                            Observable.fromIterable(users)
                                    .map(user -> userMap.put(user.getId(), user));
                        },
                        exp -> userListResponseMutableLiveData.setValue(Response.error(exp))
                ));
    }

    public void loadUser(long userId) {
        if (userMap.containsKey(userId)) {
            userMutableLiveData.setValue(Response.success(userMap.get(userId)));
            return;
        }
        compositeDisposable.add(userRepository.getUserById(userId)
        .subscribeOn(subscribeScheduler)
        .observeOn(observeScheduler)
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
        return userListResponseMutableLiveData;
    }

    public void dispose() {
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    public MutableLiveData<Response<User>> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public Single<Integer> deleteUser(User user) {
       return Single.fromCallable(()-> {
           int num = userRepository.deleteUser(user);
           if (num > 0) {
               userMap.remove(user.getId());
           }
           return num;
       });
    }
}
