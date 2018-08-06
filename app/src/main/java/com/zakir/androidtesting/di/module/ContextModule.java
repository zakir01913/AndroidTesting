package com.zakir.androidtesting.di.module;

import android.content.Context;

import com.zakir.androidtesting.di.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zakir on 26/7/18.
 */
@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @ApplicationContext
    Context applicationContext() {
        return context;
    }
}
