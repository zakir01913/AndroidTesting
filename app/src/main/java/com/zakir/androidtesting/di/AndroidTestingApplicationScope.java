package com.zakir.androidtesting.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by zakir on 27/7/18.
 */
@Scope
@Retention(RetentionPolicy.CLASS)
public @interface AndroidTestingApplicationScope {
}
