package com.zakir.androidtesting.addUser;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zakir.androidtesting.R;
import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.utils.UserTestUtils;

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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddUserActivityTest {

    @Mock
    AddUserViewModel addUserViewModel;
    MutableLiveData<Response<User>> responseMutableLiveData = new MutableLiveData<>();
    @Captor
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    private final String FIST_NAME = "First Name";
    private final String LAST_NAME = "Last Name";
    private final String EMAIL = "Email";
    private final String COMPANY = "Company";
    private final String DESIGNATION = "Designation";

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
        onView(withId(R.id.add_user_pb)).check(matches(not(isDisplayed())));
    }

    @Test
    public void save_callAddUserViewModelInsert() {
        onView(withId(R.id.first_name_et)).perform(typeText(FIST_NAME));
        onView(withId(R.id.last_name_et)).perform(typeText(LAST_NAME));
        onView(withId(R.id.email_et)).perform(typeText(EMAIL));
        onView(withId(R.id.company_et)).perform(typeText(COMPANY));
        onView(withId(R.id.designation_et)).perform(typeText(DESIGNATION), closeSoftKeyboard());

        onView(withId(R.id.save_btn)).perform(click());

        verify(addUserViewModel).insert(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertThat(user.getFirstName(), is(equalTo(FIST_NAME)));
        assertThat(user.getLastName(), is(equalTo(LAST_NAME)));
        assertThat(user.getEmail(), is(equalTo(EMAIL)));
        assertThat(user.getCompany(), is(equalTo(COMPANY)));
        assertThat(user.getDesignation(), is(equalTo(DESIGNATION)));
    }

    @Test
    public void response_withEmptyFirstName_showErrorInFirstNameEditText() {
        String errorMessage = "First name can't be empty";

        responseMutableLiveData.postValue(Response.error(
                    new AddUserException(errorMessage, AddUserException.ErrorCode.EMPTY_FIRST_NAME)));

        onView(withId(R.id.first_name_et)).check(matches(hasErrorText(errorMessage)));
    }

    @Test
    public void response_withEmptyLastName_showErrorInLastNameEditText() {
        String errorMessage = "Last name can't be empty";

        responseMutableLiveData.postValue(Response.error(
                new AddUserException(errorMessage, AddUserException.ErrorCode.EMPTY_LAST_NAME)));

        onView(withId(R.id.first_name_et)).check(matches(not(hasErrorText(errorMessage))));
        onView(withId(R.id.last_name_et)).check(matches(hasErrorText(errorMessage)));
    }

    @Test
    public void response_withInvalidEmail_showErrorInEmailEditText() {
        String errorMessage = "Email can't be empty";

        responseMutableLiveData.postValue(Response.error(
                new AddUserException(errorMessage, AddUserException.ErrorCode.INVALID_EMAIL)));

        onView(withId(R.id.first_name_et)).check(matches(not(hasErrorText(errorMessage))));
        onView(withId(R.id.last_name_et)).check(matches(not(hasErrorText(errorMessage))));
        onView(withId(R.id.email_et)).check(matches(hasErrorText(errorMessage)));
    }

    @Test
    public void response_withLoading_showProgressBar() {
        responseMutableLiveData.postValue(
                Response.loading()
        );

        onView(withId(R.id.add_user_pb)).check(matches(isDisplayed()));
    }
}