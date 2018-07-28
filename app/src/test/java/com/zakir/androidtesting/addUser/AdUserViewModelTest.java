package com.zakir.androidtesting.addUser;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.Status;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;
import com.zakir.androidtesting.utils.UserTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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

    AddUserViewModel addUserViewModel;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        addUserViewModel = new AddUserViewModel(userRepository);
    }

    @Test
    public void insertOrUpdate_withEmptyFistName_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithEmptyFirstName();

        addUserViewModel.insertOrUpdate(user);
        checkErrorResponse(addUserViewModel.response().getValue(), AddUserException.ErrorCode.EMPTY_FIRST_NAME);

    }

    @Test
    public void insertOrUpdate_withNullFistName_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithNullFirstName();

        addUserViewModel.insertOrUpdate(user);
        checkErrorResponse(addUserViewModel.response().getValue(), AddUserException.ErrorCode.EMPTY_FIRST_NAME);

    }

    @Test
    public void insertOrUpdate_withEmptyLastName_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithEmptyLastName();

        addUserViewModel.insertOrUpdate(user);

        checkErrorResponse(addUserViewModel.response().getValue(), AddUserException.ErrorCode.EMPTY_LAST_NAME);

    }

    @Test
    public void insertOrUpdate_nullEmptyLastName_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithNullLastName();

        addUserViewModel.insertOrUpdate(user);

        checkErrorResponse(addUserViewModel.response().getValue(), AddUserException.ErrorCode.EMPTY_LAST_NAME);

    }

    @Test
    public void insertOrUpdate_nullEmail_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithNullEmail();

        addUserViewModel.insertOrUpdate(user);

        checkErrorResponse(addUserViewModel.response().getValue(), AddUserException.ErrorCode.INVALID_EMAIL);

    }

    @Test
    public void insertOrUpdate_withInvalidEmail_returnAddUserException() throws Exception {
        User user = UserTestUtils.createUserWithInvalidEmail();

        addUserViewModel.insertOrUpdate(user);

        checkErrorResponse(addUserViewModel.response().getValue(), AddUserException.ErrorCode.INVALID_EMAIL);

    }

    private void checkErrorResponse(Response<User> response, AddUserException.ErrorCode errorCode) {
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), equalTo(Status.ERROR));
        assertThat(response.getError(), instanceOf(AddUserException.class));
        AddUserException addUserException = (AddUserException) response.getError();
        assertThat(addUserException.getErrorCode(), is(equalTo(errorCode)));
    }

}