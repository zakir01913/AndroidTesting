package com.zakir.androidtesting;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;
import com.zakir.androidtesting.utils.UserTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.schedulers.TestScheduler;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserViewModelTest {
    @Mock
    Observer<Response<List<User>>> userListObserver;

    @Captor
    ArgumentCaptor<Response<List<User>>> usesListResponseArgumentCaptor = ArgumentCaptor.forClass(Response.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;

    TestScheduler testScheduler = new TestScheduler();

    UserViewModel userViewModel;
    private MutableLiveData<Response<List<User>>> responseMutableLiveData;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userViewModel = new UserViewModel(userRepository, testScheduler, testScheduler );
        userViewModel.getUsersMutableLiveData().observeForever(userListObserver);
        responseMutableLiveData = userViewModel.getUsersMutableLiveData();
    }

    @Test
    public void loadUsers_respondWithLoading() {
        List<User> userList = UserTestUtils.getUsers(2);
        doReturn(Flowable.just(userList)).when(userRepository).getUsers();

        userViewModel.loadUsers();
        testScheduler.triggerActions();

        verify(userListObserver, times(2)).onChanged(usesListResponseArgumentCaptor.capture());
        Response<List<User>> response = usesListResponseArgumentCaptor.getAllValues().get(0);
        assertThat(response.getStatus(), is(equalTo(Status.LOADING)));
    }

    @Test
    public void loadUsers_responseWithListOfUser() throws Exception {
        List<User> userList = UserTestUtils.getUsers(2);
        doReturn(Flowable.just(userList)).when(userRepository).getUsers();

        userViewModel.loadUsers();

        verify(userRepository).getUsers();
        testScheduler.triggerActions();
        Response<List<User>> response = responseMutableLiveData.getValue();
        assertThat(response.getStatus(), is(equalTo(Status.SUCCESS)));
        assertThat(userList, containsInAnyOrder(response.getData().toArray()));
    }

    @Test
    public void loadUsers_withRepositoryError_responseWithError() {
        Throwable throwable = new Throwable("Repository error occurred");
        doReturn(Flowable.error(throwable)).when(userRepository).getUsers();

        userViewModel.loadUsers();
        testScheduler.triggerActions();

        Response<List<User>> response = responseMutableLiveData.getValue();
        assertThat(response.getStatus(), is(equalTo(Status.ERROR)));
    }

}