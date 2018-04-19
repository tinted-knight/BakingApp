package com.and.tim.bakingapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Stetho.InitializerBuilder builder = Stetho.newInitializerBuilder(this);
        builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));

        Stetho.Initializer initializer = builder.build();

        Stetho.initialize(initializer);
    }
}
