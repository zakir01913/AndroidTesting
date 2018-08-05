package com.zakir.androidtesting.repository;

import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.persistence.UserDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class LocalUserDataSourceImp implements UserDataSource {

    UserDao userDao;

    @Inject
    public LocalUserDataSourceImp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Flowable<List<User>> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public Flowable<User> getUserById(int userId) {
        return userDao.getUser(userId);
    }

    @Override
    public long insert(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public int delete(User user) {
        return userDao.deleteUser(user);
    }
}
