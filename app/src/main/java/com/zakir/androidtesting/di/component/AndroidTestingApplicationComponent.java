package com.zakir.androidtesting.di.component;

import com.zakir.androidtesting.addUser.AddUserActivity;
import com.zakir.androidtesting.di.module.ApplicationModule;
import com.zakir.androidtesting.di.module.ContextModule;
import com.zakir.androidtesting.user.UserListFragmentComponent;
import com.zakir.androidtesting.user.UserListFragmentModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zakir on 26/7/18.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ContextModule.class})
public interface AndroidTestingApplicationComponent {

    void inject(AddUserActivity addUserActivity);

    UserListFragmentComponent plus(UserListFragmentModule userListFragmentModule);
}
