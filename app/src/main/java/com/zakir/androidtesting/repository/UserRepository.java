package com.zakir.androidtesting.repository;

import com.zakir.androidtesting.di.LocalUserRepository;
import com.zakir.androidtesting.persistence.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by zakir on 26/7/18.
 */
public class UserRepository {
    UserDataSource userDataSource;

    @Inject
    public UserRepository(@LocalUserRepository UserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    public long insert(User user) {
        return userDataSource.insert(user);
    }

    public Flowable<List<User>> getUsers() {
       return userDataSource.getUsers();
    }

    public Flowable<User> getUserById(long userId) {
        return userDataSource.getUserById(userId);
    }

    public int deleteUser(User user) {
       return userDataSource.delete(user);
    }
}
