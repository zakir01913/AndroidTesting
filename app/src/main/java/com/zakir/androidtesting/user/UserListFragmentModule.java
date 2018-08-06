package com.zakir.androidtesting.user;

import dagger.Module;
import dagger.Provides;

@Module
public class UserListFragmentModule {
    private final UserListFragment userListFragment;

    public UserListFragmentModule(UserListFragment userListFragment) {
        this.userListFragment = userListFragment;
    }

    @Provides
    @UserListFragmentScope
    public UserListFragment userListFragment() {
        return userListFragment;
    }
}
