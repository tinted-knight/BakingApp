package com.and.tim.bakingapp.test;

import android.support.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResources {

    public static final String RESOURCE = "baking_app_test";

    private static CountingIdlingResource idlingResource = new CountingIdlingResource(RESOURCE);

    public static CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

    public static void increment() {
        idlingResource.increment();
    }

    public static void decrement() {
        idlingResource.decrement();
    }

}
