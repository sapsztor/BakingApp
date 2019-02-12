package com.dwbi.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.dwbi.bakingapp.model.Ingredient;
import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.R;

import java.util.List;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
//public class IngredActivity extends AppCompatActivity implements IngredFragment.OnStepSelectedListener {
 public class IngredActivity extends AppCompatActivity  {

    private static final String TAG = "PSX";
    Bundle argument;
    private Recipe mRecipe;

    String mLayout_Config;
    Boolean mTwoPane;

    private Fragment mIngred = null;
    private Fragment mVideo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.ingred_activity_container_layout);


        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }


        mLayout_Config  = getResources().getString(R.string.layout_config);
        switch (mLayout_Config) {
            case "portrait":
                mTwoPane = false;
                Log.d(TAG, "IngredActivity-> " + mLayout_Config);
                break;
            case "landscape" :
                mTwoPane = false;
                Log.d(TAG, "IngredActivity-> " + mLayout_Config);
                break;
            case "tablet_landscape":
                mTwoPane = true;
                Log.d(TAG, "IngredActivity-> " + mLayout_Config);
                break;
            default:
                Log.d(TAG, "IngredActivity-> " + mLayout_Config);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.


            if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey(IngredFragment.ARG_ITEM_ID) ){
                //Bundle bundle = this.getIntent().getBundleExtra("recipe");
                argument = this.getIntent().getBundleExtra("recipe");
                mRecipe = argument.getParcelable(IngredFragment.ARG_ITEM_ID);

                getSupportActionBar().setTitle(mRecipe.getName());
                List<Ingredient> ingredients = mRecipe.getIngredients();

            } else {
                finish();
            }

        } else {
            mRecipe = savedInstanceState.getParcelable("RECIPE");

            getSupportActionBar().setTitle(savedInstanceState.getString("RECIPENAME"));
            List<Ingredient> ingredients = mRecipe.getIngredients();

            argument = new Bundle();
            argument.putParcelable(IngredFragment.ARG_ITEM_ID, mRecipe);

        }

        if (mTwoPane){

            IngredFragment ingredFragment = new IngredFragment();
            ingredFragment.setArguments(argument);
            //fragment.setOnStepSelectedListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, ingredFragment)
                    //.addToBackStack(null)
                    .commit();


            Bundle stepfragment_arg = new Bundle();
            BlankFragment blankFragment = new BlankFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.video_view_container, blankFragment)
                    //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    //.addToBackStack(null)
                    .commit();

        } else {

            IngredFragment ingredFragment = new IngredFragment();
            ingredFragment.setArguments(argument);
            //fragment.setOnStepSelectedListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, ingredFragment)
                    //.addToBackStack(null)
                    .commit();

        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("RECIPE", mRecipe);
        outState.putString("RECIPENAME", mRecipe.getName());
    }
}
