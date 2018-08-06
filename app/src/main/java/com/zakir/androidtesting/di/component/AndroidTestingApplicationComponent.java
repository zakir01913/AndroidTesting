package com.zakir.androidtesting.di.component;

import com.zakir.androidtesting.di.ObserverScheduler;
import com.zakir.androidtesting.di.SubscribeScheduler;
import com.zakir.androidtesting.di.module.ApplicationModule;
import com.zakir.androidtesting.di.module.ContextModule;
import com.zakir.androidtesting.persistence.UserDao;
import com.zakir.androidtesting.persistence.UserDatabase;
import com.zakir.androidtesting.repository.UserDataSource;
import com.zakir.androidtesting.user.UserListFragmentComponent;
import com.zakir.androidtesting.user.UserListFragmentModule;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.Scheduler;

/**
 * Created by zakir on 26/7/18.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ContextModule.class})
public interface AndroidTestingApplicationComponent {

    UserListFragmentComponent plus(UserListFragmentModule userListFragmentModule);
}
