package com.dwbi.bakingapp;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.dwbi.bakingapp.ui.IngredActivity;
import com.dwbi.bakingapp.ui.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntentTesting {


    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityTestRule = new IntentsTestRule<>(RecipeListActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void checkIntent_IngredActivity() {
        onView(ViewMatchers.withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        intended(hasComponent(IngredActivity.class.getName()));

    }
//    @Test
//    public void checkIntent_RecipeBundle() {
//        onView(ViewMatchers.withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
//        intended(assertThat(intent));
//
//    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
