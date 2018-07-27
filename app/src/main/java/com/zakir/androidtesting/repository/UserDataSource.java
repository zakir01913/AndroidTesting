package com.zakir.androidtesting.repository;

import com.zakir.androidtesting.persistence.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by zakir on 26/7/18.
 */

public interface UserDataSource {

    Flowable<List<User>> getUsers();

    Flowable<User> getUserById(int userId);

    Completable insertOrUpdate(User user);

    Completable delete(User user);
}
