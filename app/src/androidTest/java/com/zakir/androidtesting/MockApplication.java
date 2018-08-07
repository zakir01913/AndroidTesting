package com.zakir.androidtesting;

import com.zakir.androidtesting.user.TestUserListFragmentComponent;
import com.zakir.androidtesting.user.UserListFragment;
import com.zakir.androidtesting.user.UserListFragmentModule;

public class MockApplication extends AndroidTestingApplication {

    TestAppComponent testAppComponent;
    TestUserListFragmentComponent testUserListFragmentComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        testAppComponent =  DaggerTestAppComponent.builder().build();
    }

    @Override
    public TestAppComponent getAndroidTestingApplicationComponent() {
        return testAppComponent;
    }

    @Override
    public TestUserListFragmentComponent createUserListFragmentComponent(UserListFragment userListFragment) {
        testUserListFragmentComponent = testAppComponent.plus();
        return testUserListFragmentComponent;
    }
}
