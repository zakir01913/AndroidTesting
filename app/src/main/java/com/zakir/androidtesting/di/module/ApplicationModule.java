package com.zakir.androidtesting.di.module;

import android.content.Context;

import com.zakir.androidtesting.UserViewModel;
import com.zakir.androidtesting.UserViewModelFactory;
import com.zakir.androidtesting.di.ApplicationContext;
import com.zakir.androidtesting.di.LocalUserRepository;
import com.zakir.androidtesting.di.ObserverScheduler;
import com.zakir.androidtesting.di.SubscribeScheduler;
import com.zakir.androidtesting.persistence.UserDao;
import com.zakir.androidtesting.persistence.UserDatabase;
import com.zakir.androidtesting.repository.LocalUserDataSourceImp;
import com.zakir.androidtesting.repository.UserDataSource;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module(includes = ContextModule.class)
public class ApplicationModule {

    @Provides
    @SubscribeScheduler
    public Scheduler subscribeScheduler() {
        return Schedulers.io();
    }

    @Provides
    @ObserverScheduler
    public Scheduler observerScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    public UserDao userDao(UserDatabase userDatabase) {
        return userDatabase.userDao();
    }

    @Provides
    public UserDatabase database(@ApplicationContext Context context) {
       return UserDatabase.getInstance(context);
    }

    @Provides
    @LocalUserRepository
    public UserDataSource userDataSource(UserDao userDao){
        return new LocalUserDataSourceImp(userDao);
    }

    @Provides
    public UserViewModelFactory userViewModelFactory(UserViewModel userViewModel) {
        return new UserViewModelFactory(userViewModel);
    }

}
