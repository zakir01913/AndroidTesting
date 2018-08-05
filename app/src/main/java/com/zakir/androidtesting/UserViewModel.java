package com.zakir.androidtesting;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

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

    @Inject
    UserRepository userRepository;
    MutableLiveData<Response<List<User>>> responseMutableLiveData = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Scheduler subscribeSchedular, observeSchedular;
    private MutableLiveData<Response<User>> userMutableLiveData = new MutableLiveData<>();
    private Map<Long, User> userMap = new HashedMap();

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
