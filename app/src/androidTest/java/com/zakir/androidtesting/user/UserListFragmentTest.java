package com.zakir.androidtesting.user;

import android.app.Instrumentation;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zakir.androidtesting.MockApplication;
import com.zakir.androidtesting.R;
import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.UserViewModel;
import com.zakir.androidtesting.UserViewModelFactoryType;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.testing.SingleFragmentTestActivity;
import com.zakir.androidtesting.utils.UserTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserListFragmentTest {

    @Inject
    @UserViewModelFactoryType
    public ViewModelProvider.Factory viewModelFactory;

    @Mock
    UserViewModel userViewModel;

    private MutableLiveData<Response<List<User>>> responseMutableLiveData =
            new MutableLiveData<>();

    @Rule
    public ActivityTestRule<SingleFragmentTestActivity> testActivityActivityTestRule =
            new ActivityTestRule<>(SingleFragmentTestActivity.class,
                   true,
                    false);
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    UserListFragment userListFragment = new UserListFragment();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        ((MockApplication)instrumentation.getTargetContext().getApplicationContext())
                .getAndroidTestingApplicationComponent().inject(this);

        when(viewModelFactory.create(any())).thenReturn(userViewModel);
        when(userViewModel.getUsersMutableLiveData()).thenReturn(responseMutableLiveData);
    }


    @Test
    public void response_withLoadingStatus_showUserLoaderPB() {
        lunchActivity();

        responseMutableLiveData.postValue(Response.loading());

        onView(withId(R.id.user_list_pb)).check(matches(isDisplayed()));
    }

    @Test
    public void response_withUserList_shownInRecyclerView() {
        lunchActivity();
        List<User> userList = UserTestUtils.getUsers(2);
        responseMutableLiveData.postValue(Response.success(userList));
        String name = userList.get(0).getFullName();
        String name1 = userList.get(1).getFullName();

        onView(withText(name)).check(matches(isDisplayed()));
        onView(withText(name1)).check(matches(isDisplayed()));
        onView(withId(R.id.user_list_pb)).check(matches(not(isDisplayed())));
    }

    @Test
    public void pressDeleteBtn_removeUserFromList() {
        lunchActivity();
        List<User> userList = UserTestUtils.getUsers(2);
        User user = userList.get(0);
        when(userViewModel.deleteUser(user)).thenAnswer(invocation -> {
            userList.remove(user);
            responseMutableLiveData.postValue(Response.success(copy(userList)));
            return Single.just(1);
        });

        responseMutableLiveData.postValue(Response.success(copy(userList)));
        onView(withText(user.getFullName())).check(matches(isDisplayed()));

        onView(withId(R.id.user_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(
                0,
                new UserListDeleteViewAction(R.id.delete_iv)));

        verify(userViewModel).deleteUser(user);
        onView(withText(user.getFullName())).check(doesNotExist());
    }

    private void lunchActivity() {
        testActivityActivityTestRule.launchActivity(new Intent());
        testActivityActivityTestRule.getActivity().setFragment(userListFragment);
    }

    private List<User> copy(List<User> users) {
        List<User> userList = new ArrayList<>();
        for (User user : users) {
            userList.add(user);
        }

        return userList;
    }
}