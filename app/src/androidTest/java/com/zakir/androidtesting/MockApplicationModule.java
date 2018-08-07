package com.zakir.androidtesting;

import android.arch.lifecycle.ViewModelProvider;

import com.zakir.androidtesting.di.LocalUserRepository;
import com.zakir.androidtesting.di.ObserverScheduler;
import com.zakir.androidtesting.di.SubscribeScheduler;
import com.zakir.androidtesting.persistence.UserDao;
import com.zakir.androidtesting.persistence.UserDatabase;
import com.zakir.androidtesting.repository.UserDataSource;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class MockApplicationModule {

    @Provides
    @SubscribeScheduler
    public Scheduler subscribeScheduler() {
        return Mockito.mock(Scheduler.class);
    }

    @Singleton
    @Provides
    @ObserverScheduler
    public Scheduler observerScheduler() {
        return Mockito.mock(Scheduler.class);
    }

    @Singleton
    @Provides
    public UserDao userDao() {
        return Mockito.mock(UserDao.class);
    }

    @Singleton
    @Provides
    public UserDatabase database() {
        return Mockito.mock(UserDatabase.class);
    }

    @Singleton
    @Provides
    @LocalUserRepository
    public UserDataSource userDataSource(){
        return Mockito.mock(UserDataSource.class);
    }

    @Singleton
    @Provides
    @UserViewModelFactoryType
    public ViewModelProvider.Factory userViewModelFactory() {
        return Mockito.mock(UserViewModelFactory.class);
    }
}
