package com.zakir.androidtesting.addUser;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.Status;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;
import com.zakir.androidtesting.utils.UserTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.schedulers.TestScheduler;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by zakir on 27/7/18.
 */
public class AdUserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;

    @Mock
    Observer<Response<User>> responseObserver;

    @Captor
    ArgumentCaptor<Response<User>> argumentCaptor = ArgumentCaptor.forClass(Response.class);

    AddUserViewModel addUserViewModel;

    TestScheduler testScheduler = new TestScheduler();

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        addUserViewModel = new AddUserViewModel(userRepository, testScheduler, testScheduler);
        addUserViewModel.response().observeForever(responseObserver);
    }

    @Test
    public void insert_withUser_returnLoading() {
        User user = UserTestUtils.createValidUser();

        addUserViewModel.insert(user);

        verify(responseObserver, times(2)).onChanged(argumentCaptor.capture());
        List<Response<User>> responses = argumentCaptor.getAllValues();
        assertThat(responses.get(0).getStatus(), is(equalTo(Status.LOADING)));
    }

    @Test
    public void insert_withEmptyFistName_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithEmptyFirstName();

        addUserViewModel.insert(user);

        verify(responseObserver, times(1)).onChanged(argumentCaptor.capture());
        checkErrorResponse(argumentCaptor.getValue(), AddUserException.ErrorCode.EMPTY_FIRST_NAME);

    }

    @Test
    public void insert_withNullFistName_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithNullFirstName();

        addUserViewModel.insert(user);

        verify(responseObserver, times(1)).onChanged(argumentCaptor.capture());
        checkErrorResponse(argumentCaptor.getValue(), AddUserException.ErrorCode.EMPTY_FIRST_NAME);

    }

    @Test
    public void insert_withEmptyLastName_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithEmptyLastName();
        
        addUserViewModel.insert(user);

        verify(responseObserver, times(1)).onChanged(argumentCaptor.capture());
        checkErrorResponse(argumentCaptor.getValue(), AddUserException.ErrorCode.EMPTY_LAST_NAME);

    }

    @Test
    public void insert_nullEmptyLastName_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithNullLastName();

        addUserViewModel.insert(user);

        verify(responseObserver, times(1)).onChanged(argumentCaptor.capture());
        checkErrorResponse(argumentCaptor.getValue(), AddUserException.ErrorCode.EMPTY_LAST_NAME);

    }

    @Test
    public void insert_nullEmail_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithNullEmail();

        addUserViewModel.insert(user);

        verify(responseObserver, times(1)).onChanged(argumentCaptor.capture());
        checkErrorResponse(argumentCaptor.getValue(), AddUserException.ErrorCode.INVALID_EMAIL);

    }

    @Test
    public void insert_withInvalidEmail_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithInvalidEmail();

        addUserViewModel.insert(user);

        verify(responseObserver, times(1)).onChanged(argumentCaptor.capture());
        checkErrorResponse(argumentCaptor.getValue(), AddUserException.ErrorCode.INVALID_EMAIL);

    }

    private void checkErrorResponse(Response<User> response, AddUserException.ErrorCode errorCode) {
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), equalTo(Status.ERROR));
        assertThat(response.getError(), instanceOf(AddUserException.class));
        AddUserException addUserException = (AddUserException) response.getError();
        assertThat(addUserException.getErrorCode(), is(equalTo(errorCode)));
    }

    @Test
    public void insert_withValidUserData_returnSuccess() {
        User user = UserTestUtils.createValidUser();
        when(userRepository.insert(ArgumentMatchers.any(User.class))).thenReturn(1l);

        addUserViewModel.insert(user);

        verify(responseObserver, times(2)).onChanged(argumentCaptor.capture());
        Response<User> response = argumentCaptor.getValue();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(equalTo(Status.SUCCESS)));
        User user1 = response.getData();
        assertThat(user1.getId(), is(equalTo(1l)));
        assertThat(UserTestUtils.compareUser(user, user1), is(equalTo(true)));
    }

}