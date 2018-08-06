package com.zakir.androidtesting.user;

import com.zakir.androidtesting.di.component.AndroidTestingApplicationComponent;

import dagger.Component;
import dagger.Subcomponent;

@UserListFragmentScope
@Subcomponent(modules = UserListFragmentModule.class)
public interface UserListFragmentComponent {

    void injectUserListFragment(UserListFragment userListFragment);
}
