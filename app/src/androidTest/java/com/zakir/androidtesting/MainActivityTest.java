package com.zakir.androidtesting;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zakir.androidtesting.addUser.AddUserActivity;
import com.zakir.androidtesting.persistence.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;

/**
 * Created by Zakir Hossain on 8/4/18.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Mock
    UserViewModel userViewModel;

    MutableLiveData<Response<List<User>>> responseMutableLiveData = new MutableLiveData<>();

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(userViewModel.getUsersMutableLiveData()).thenReturn(responseMutableLiveData);
    }


    @Test
    public void clickAddFabButton_openAddUserActivity() {
        onView(withId(R.id.add_user_fab)).perform(click());
        intended(hasComponent(AddUserActivity.class.getName()));
    }

    @Test
    public void mainActivity_hasUserListPlaceHolder() {
        onView(withId(R.id.user_list)).check(matches(isDisplayed()));
    }

    @Test
    public void response_withLoadingStatus_showLoadingProgressBar() {
        responseMutableLiveData.setValue(Response.loading());

        onView(withId(R.id.user_list_pb)).check(matches(isDisplayed()));
    }

}