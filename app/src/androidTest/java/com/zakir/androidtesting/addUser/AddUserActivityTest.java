package com.zakir.androidtesting.addUser;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddUserActivityTest {

    @Mock
    AddUserViewModel addUserViewModel;

    @Rule
    ActivityTestRule<AddUserActivity> addUserActivityActivityTestRule = new ActivityTestRule<>(
            AddUserActivity.class
    );

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        addUserActivityActivityTestRule.getActivity().addUserViewModel = addUserViewModel;
    }

    @Test
    public void save_withInvalidFirstName_showErrorInFirstNameEditText() {

    }
}