package com.zakir.androidtesting.user;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zakir.androidtesting.R;
import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.UserViewModel;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.testing.SingleFragmentTestActivity;
import com.zakir.androidtesting.util.ViewModelUtil;
import com.zakir.androidtesting.utils.UserTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserDetailFragmentTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule
    public ActivityTestRule<SingleFragmentTestActivity> testActivityActivityTestRule =
            new ActivityTestRule<>(SingleFragmentTestActivity.class);

    private UserDetailFragment userDetailFragment = new UserDetailFragment();

    private final User user = UserTestUtils.createValidUser();

    @Mock
    UserViewModel userViewModel;

    MutableLiveData<Response<User>> userMutableLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(userViewModel.getUserMutableLiveData()).thenReturn(userMutableLiveData);

        userDetailFragment.viewModelFactory = ViewModelUtil.createFactory(userViewModel);
        testActivityActivityTestRule.getActivity().setFragment(userDetailFragment);
    }

    @Test
    public void loadUser_withValidUser_showUserDetailInUI() {
        userMutableLiveData.postValue(Response.success(user));
        String name = user.getFirstName() + " " + user.getLastName();

        onView(withId(R.id.user_name_tv)).check(matches(withText(name)));
        onView(withId(R.id.user_email_tv)).check(matches(withText(user.getEmail())));
        onView(withId(R.id.user_company_tv)).check(matches(withText(user.getCompany())));
        onView(withId(R.id.user_designation_tv)).check(matches(withText(user.getDesignation())));
    }

    @Test
    public void loadUser_withError_showErrorMessage() {
        Throwable throwable = new Throwable("Unable to load user data");
        userMutableLiveData.postValue(Response.error(throwable));

        onView(withId(R.id.user_detail_group)).check(matches(not(isDisplayed())));
        onView(withId(R.id.user_detail_error_tv)).check(matches(isDisplayed()));
    }
}