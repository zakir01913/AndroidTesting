package com.zakir.androidtesting.user;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

public class UserListDeleteViewAction implements ViewAction {
    int viewId;

    public UserListDeleteViewAction(int viewId) {
        this.viewId = viewId;
    }

    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void perform(UiController uiController, View view) {
        View view1 = view.findViewById(viewId);
        view1.performClick();
    }
}
