package com.zakir.androidtesting.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
interface UserDao {

    @Query("SELECT * FROM USERS")
    Flowable<List<User>> getUsers();

    @Query("SELECT * FROM USERS WHERE id== :userId")
    Flowable<User> getUser(long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);

    @Delete
    void deleteUser(User user);
}
