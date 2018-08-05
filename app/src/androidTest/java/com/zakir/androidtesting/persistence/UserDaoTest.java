package com.zakir.androidtesting.persistence;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zakir.androidtesting.utils.UserTestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private UserDatabase userDatabase;

    @Before
    public void setup() {
        userDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                UserDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        userDatabase.close();
    }

    @Test
    public void getUsers_whenNoUserInserted_returnEmptyList() {
        userDatabase.userDao().getUsers()
                .test()
                .assertValue(users -> users.isEmpty());
    }

    @Test
    public void insert_withValidUser_userInserted() {
        User user = UserTestUtils.createValidUser();

        long id = userDatabase.userDao().insertUser(user);

        userDatabase.userDao().getUsers()
                .test()
                .assertValue(users -> {
                    user.setId(id);
                    User user1 = users.get(0);
                    return isUserEqual(user, user1);
                });
    }

    @Test
    public void getUser_withValidId_returnUser() {
        User user = UserTestUtils.createValidUser();
        long id = userDatabase.userDao().insertUser(user);
        user.setId(id);

        userDatabase.userDao().getUser(id)
                .test()
                .assertValue(user1 -> {
                    return isUserEqual(user, user1);
                });

    }

    @Test
    public void delete_withValidUser_userDeleted() {
        User user = UserTestUtils.createValidUser();
        long id = userDatabase.userDao().insertUser(user);
        user.setId(id);

        userDatabase.userDao().deleteUser(user);

        userDatabase.userDao().getUser(id)
                .test()
                .assertNoValues();
    }

    private boolean isUserEqual(User user1, User user2) {
        boolean flag = true;
        flag &= user1.getId() == user2.getId();
        flag &= UserTestUtils.compareUser(user1, user2);
        return flag;
    }
}