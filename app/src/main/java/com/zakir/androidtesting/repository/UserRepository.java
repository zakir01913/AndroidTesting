package com.zakir.androidtesting.repository;

import com.zakir.androidtesting.di.AndroidTestingApplicationScope;
import com.zakir.androidtesting.persistence.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by zakir on 26/7/18.
 */
@AndroidTestingApplicationScope
public class UserRepository {
    UserDataSource userDataSource;

    @Inject
    public UserRepository(UserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    public long insert(User user) {
        return userDataSource.insert(user);
    }

    public Flowable<List<User>> getUsers() {
       return userDataSource.getUsers();
    }
}
