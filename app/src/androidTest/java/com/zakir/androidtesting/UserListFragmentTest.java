package com.zakir.androidtesting;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.testing.SingleFragmentTestActivity;
import com.zakir.androidtesting.user.UserListFragment;
import com.zakir.androidtesting.util.ViewModelUtil;
import com.zakir.androidtesting.utils.UserTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserListFragmentTest {

    @Mock
    UserViewModel userViewModel;

    private MutableLiveData<Response<List<User>>> responseMutableLiveData =
            new MutableLiveData<>();

    @Rule
    public ActivityTestRule<SingleFragmentTestActivity> testActivityActivityTestRule =
            new ActivityTestRule<>(SingleFragmentTestActivity.class);
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    UserListFragment userListFragment = new UserListFragment();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(userViewModel.getResponseMutableLiveData()).thenReturn(responseMutableLiveData);

        userListFragment.viewModelFactory = ViewModelUtil.createFactory(userViewModel);
        testActivityActivityTestRule.getActivity().setFragment(userListFragment);
    }

    @Test
    public void response_withLoadingStatus_showUserLoaderPB() {
        responseMutableLiveData.postValue(Response.loading());

        onView(withId(R.id.user_list_pb)).check(matches(isDisplayed()));
    }

    @Test
    public void response_withUserList_shownInRecyclerView() {
        List<User> userList = UserTestUtils.getUsers(2);
        responseMutableLiveData.postValue(Response.success(userList));
        String name = userList.get(0).getFirstName() + " " + userList.get(0).getLastName();
        String name1 = userList.get(1).getFirstName() + " " + userList.get(1).getLastName();

        onView(withText(name)).check(matches(isDisplayed()));
        onView(withText(name1)).check(matches(isDisplayed()));
        onView(withId(R.id.user_list_pb)).check(matches(not(isDisplayed())));
    }
}