package com.zakir.androidtesting;

import android.app.Application;
import android.content.Context;

import com.zakir.androidtesting.di.component.AndroidTestingApplicationComponent;
import com.zakir.androidtesting.di.component.DaggerAndroidTestingApplicationComponent;
import com.zakir.androidtesting.di.module.ContextModule;
import com.zakir.androidtesting.user.UserListFragment;
import com.zakir.androidtesting.user.UserListFragmentComponent;
import com.zakir.androidtesting.user.UserListFragmentModule;

/**
 * Created by zakir on 26/7/18.
 */

public class AndroidTestingApplication extends Application {

    private AndroidTestingApplicationComponent androidTestingApplicationComponent;
    private UserListFragmentComponent userListFragmentComponent;

    public static AndroidTestingApplication get(Context context) {
        return (AndroidTestingApplication) context.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        androidTestingApplicationComponent = DaggerAndroidTestingApplicationComponent
                .builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    public AndroidTestingApplicationComponent getAndroidTestingApplicationComponent() {
        return androidTestingApplicationComponent;
    }

    public UserListFragmentComponent createUserListFragmentComponent(UserListFragment userListFragment) {
        userListFragmentComponent = androidTestingApplicationComponent
                .plus(new UserListFragmentModule(userListFragment));
        return userListFragmentComponent;
    }

    public void realeaseUserListFragmentComponent() {
        userListFragmentComponent = null;
    }
}
