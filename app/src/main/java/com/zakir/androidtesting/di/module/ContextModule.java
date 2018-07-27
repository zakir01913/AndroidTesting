package com.zakir.androidtesting.di.module;

import android.content.Context;

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
    Context applicationContext() {
        return context;
    }
}
