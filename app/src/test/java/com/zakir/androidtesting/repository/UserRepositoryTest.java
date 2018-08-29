package com.zakir.androidtesting.repository;

import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.utils.UserTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertThat;

public class UserRepositoryTest {

    UserRepository userRepository;

    @Mock
    UserDataSource userDataSource;
    @Captor
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    ArgumentCaptor<List<User>> userListArgumentCaptor = ArgumentCaptor.forClass(List.class);
    TestSubscriber<List<User>> getUsersTestSubscriber;
    TestSubscriber<User> getUserByIdTestSubscriber;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userRepository = new UserRepository(userDataSource);
        getUsersTestSubscriber = new TestSubscriber<>();
        getUserByIdTestSubscriber = new TestSubscriber<>();
    }

    @Test
    public void insert_user_callUserDataSourceInsert() {
        User user = UserTestUtils.createValidUser();
        when(userDataSource.insert(user)).thenReturn(1l);

        long id = userRepository.insert(user);

        verify(userDataSource).insert(userArgumentCaptor.capture());
        assertTrue(UserTestUtils.compareUser(userArgumentCaptor.getValue(), user));
        assertThat(id, is(comparesEqualTo(1l)));
    }

    @Test
    public void getUsers_void_returnValidFlowable() {
        List<User> userList = UserTestUtils.getUsers(2);
        doReturn(Flowable.just(userList)).when(userDataSource).getUsers();

        userRepository.getUsers().subscribe(getUsersTestSubscriber);

        verify(userDataSource).getUsers();
        getUsersTestSubscriber.assertValue(userList);
    }

    @Test
    public void getUserById_withUserId_returnValidFlowable() {
        User user = UserTestUtils.createValidUser();
        user.setId(1);
        doReturn(Flowable.just(user)).when(userDataSource).getUserById(user.getId());

        userRepository.getUserById(user.getId()).subscribe(getUserByIdTestSubscriber);

        verify(userDataSource).getUserById(user.getId());
        getUserByIdTestSubscriber.assertValue(user);
    }

    @Test
    public void delete_user_returnNumberOfItemDeleted() {
        User user = UserTestUtils.createValidUser();
        when(userDataSource.delete(user)).thenReturn(1);

        int num = userRepository.deleteUser(user);

        verify(userDataSource).delete(user);
        assertThat(num, is(comparesEqualTo(1)));
    }
}