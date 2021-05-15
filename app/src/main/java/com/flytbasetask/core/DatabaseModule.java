package com.flytbasetask.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    AppState provideAppState(SharedPreferences sharedPreferences) {
        return new AppState(sharedPreferences);
    }

}
