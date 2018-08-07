package com.zakir.androidtesting;

import com.zakir.androidtesting.addUser.AddUserActivity;
import com.zakir.androidtesting.di.component.AndroidTestingApplicationComponent;
import com.zakir.androidtesting.user.TestUserListFragmentComponent;
import com.zakir.androidtesting.user.UserDetailFragmentTest;
import com.zakir.androidtesting.user.UserListFragment;
import com.zakir.androidtesting.user.UserListFragmentModule;
import com.zakir.androidtesting.user.UserListFragmentTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MockApplicationModule.class)
public interface TestAppComponent extends AndroidTestingApplicationComponent {
    void inject(UserListFragmentTest userListFragmentTest);

    TestUserListFragmentComponent plus();

    void inject(UserDetailFragmentTest userDetailFragmentTest);
}
