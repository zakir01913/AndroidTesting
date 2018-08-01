package com.zakir.androidtesting.addUser;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zakir.androidtesting.R;
import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.persistence.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddUserActivityTest {

    @Mock
    AddUserViewModel addUserViewModel;
    @Mock
    MutableLiveData<Response<User>> responseMutableLiveData;
    @Captor
    ArgumentCaptor<Observer<Response<User>>> observerArgumentCaptor;

    @Rule
    public ActivityTestRule<AddUserActivity> addUserActivityActivityTestRule = new ActivityTestRule<>(
            AddUserActivity.class
    );

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        addUserActivityActivityTestRule.getActivity().addUserViewModel = addUserViewModel;
        when(addUserViewModel.response()).thenReturn(responseMutableLiveData);
        addUserActivityActivityTestRule.getActivity().setAddUserViewModel(addUserViewModel);
        verify(responseMutableLiveData).observe(isA(LifecycleOwner.class), observerArgumentCaptor.capture());
    }

    @Test
    public void save_callAddUserViewModelInsert() {

        onView(ViewMatchers.withId(R.id.save_btn)).perform(click());

        verify(addUserViewModel).insert(null);
    }

    @Test
    public void save_withEmptyFirstName_showError() {

    }
}