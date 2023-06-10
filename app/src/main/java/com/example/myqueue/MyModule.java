package com.example.myqueue;

import android.app.Application;
import android.content.Context;

import com.example.myqueue.api.WebRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MyModule {

    @Provides
    public Repository provideRepository(@ApplicationContext Context context) {
        return new WebRepository(context);
    }
}

