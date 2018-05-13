package com.and.tim.bakingapp.ui.main;


import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.test.EspressoIdlingResources;
import com.and.tim.bakingapp.ui.step_list.StepListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StepListActivityTest {

    private static final String RECIPE_KEY = "recipe_id";
    private static final int RECIPE_ID = 3;
    private static final String STEP_LIST_CAPTION = "13 steps to make Yellow Cake";

    @Rule public ActivityTestRule<StepListActivity> activityTestRule
            = new ActivityTestRule<>(StepListActivity.class, true, false);
    private CountingIdlingResource idlingResource;

    @Before
    public void before() {
        Intent intent = new Intent();
        intent.putExtra(RECIPE_KEY, RECIPE_ID);
        intent.setAction(StepListActivity.ACTION_STEP_LIST);
        activityTestRule.launchActivity(intent);

        idlingResource = EspressoIdlingResources.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void after() {
        IdlingRegistry.getInstance().unregister(idlingResource);
        activityTestRule.finishActivity();
    }

    @Test
    public void stepListTest_caption() {
        onView(withId(R.id.tvName)).check(matches(withText(STEP_LIST_CAPTION)));
    }

    @Test
    public void stepListTest_clickFromFirstToLast() {
        // Click first item, prev button not active
        onView(withId(R.id.rvStepList)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.btnNext)).check(matches(isEnabled()));
        onView(withId(R.id.btnPrev)).check(matches(not(isEnabled())));

        for (int i = 1; i < 12; i++) {
            onView(withId(R.id.btnNext)).perform(click());
            onView(withId(R.id.btnNext)).check(matches(isEnabled()));
            onView(withId(R.id.btnPrev)).check(matches(isEnabled()));
        }
        // Click next last time, next button not active
        onView(withId(R.id.btnNext)).perform(click());
        onView(withId(R.id.btnNext)).check(matches(not(isEnabled())));
        onView(withId(R.id.btnPrev)).check(matches(isEnabled()));
    }

}
