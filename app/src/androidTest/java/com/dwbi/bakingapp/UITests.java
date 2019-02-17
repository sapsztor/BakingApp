package com.dwbi.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dwbi.bakingapp.ui.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITests {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class, true, true);



    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void checkRecipenameVisibility(){
        onView(withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Brownies")).check(matches(isDisplayed()));
    }


    @Test
    public void checkRecipenameVisibility2(){
        onView(withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText("Cheesecake")).check(matches(isDisplayed()));
        //onView(ViewMatchers.withId(R.id.rv_recipe_steps)).perform(RecyclerViewActions.scrollToPosition(0));
    }


    @Test
    public void checkExoIsVisible() {
        onView(withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.rv_recipe_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.exoView)).check(matches(isDisplayed()));
    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
