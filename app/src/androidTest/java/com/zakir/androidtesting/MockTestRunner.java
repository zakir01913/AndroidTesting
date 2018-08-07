package com.zakir.androidtesting;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String testApplicationClassName = MockApplication.class.getCanonicalName();
        return super.newApplication(cl, testApplicationClassName, context);
    }
}
