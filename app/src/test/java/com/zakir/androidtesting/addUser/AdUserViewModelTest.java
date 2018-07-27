package com.zakir.androidtesting.addUser;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.Status;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

/**
 * Created by zakir on 27/7/18.
 */
public class AdUserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;

    AddUserViewModel addUserViewModel;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        addUserViewModel = new AddUserViewModel(userRepository);
    }

    @Test
    public void addUser_withEmptyFistName_returnAddUserException() throws InterruptedException {
        User user = new User("", "lastName", "email");

        addUserViewModel.insertOrUpdate(user);

        Response<User>  response = addUserViewModel.response().getValue();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), equalTo(Status.ERROR));
        assertThat(response.getError(), instanceOf(AddUserException.class));
        AddUserException addUserException = (AddUserException) response.getError();
        assertThat(addUserException.getErrorCode(), equalTo(AddUserException.ErrorCode.EMPTY_FIRST_NAME));

    }

}