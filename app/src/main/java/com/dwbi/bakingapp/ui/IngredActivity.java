package com.dwbi.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.dwbi.bakingapp.adapter.StepsAdapter;
import com.dwbi.bakingapp.model.Ingredient;
import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.R;
import com.dwbi.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
//public class IngredActivity extends AppCompatActivity implements IngredFragment.OnStepSelectedListener {
 public class IngredActivity extends AppCompatActivity implements StepsAdapter.onClickListener {

    private static final String TAG = "INGREDACT";
    static String BACKSTACK_INGRED_FRAG = "BACKSTACK_INGRED_FRAG";
    static String BACKSTACK_VIDEO_FRAG = "BACKSTACK_VIDEO_FRAG";



    Bundle mArgument;
    private Recipe mRecipe;
    private String mRecipeName;
    private ArrayList<Ingredient> mIngredients = new ArrayList<>();
    private ArrayList<Step> mSteps = new ArrayList<>();
    private int mSelectedStep = 0;


    String mLayout_Config;
    Boolean mTwoPane;

    final IngredFragment mIngredFragment = new IngredFragment();
    final StepVideoFragment mVideoFragment = new StepVideoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ingred_activity_container_layout);

        mTwoPane = isTwoPane();


        if (savedInstanceState == null) {
            if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey(RecipeListActivity.ARG_BUNDLE) ){
                Log.d(TAG, "2 IngredActivity ");
                mArgument = this.getIntent().getBundleExtra(RecipeListActivity.ARG_BUNDLE);
                mRecipe = mArgument.getParcelable(RecipeListActivity.ARG_RECIPE);
                mIngredients = mRecipe.getIngredients();
                mSteps = mRecipe.getSteps();
                mRecipeName = mRecipe.getName();

            }


            final IngredFragment ingredFragment = new IngredFragment();
            ingredFragment.setArguments(mArgument);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, ingredFragment, "ingredFragment")
                    .addToBackStack(BACKSTACK_INGRED_FRAG)
                    .commit();


            if (mTwoPane){
                StepVideoFragment videoFragment = new StepVideoFragment();
                videoFragment.setArguments(mArgument);


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.video_view_container, videoFragment, "videoFragment")
                        .addToBackStack(BACKSTACK_VIDEO_FRAG)
                        .commit();


            }
        } else {
            mRecipe = savedInstanceState.getParcelable(RecipeListActivity.ARG_RECIPE);
            mRecipeName = savedInstanceState.getString(RecipeListActivity.ARG_RECIPE_NAME);
            mSelectedStep = savedInstanceState.getInt(RecipeListActivity.ARG_SELECTED_STEP);

            mIngredients = mRecipe.getIngredients();
            mSteps = mRecipe.getSteps();

            mArgument = new Bundle();
            mArgument.putParcelable(RecipeListActivity.ARG_RECIPE, mRecipe);
            mArgument.putInt(RecipeListActivity.ARG_SELECTED_STEP, mSelectedStep);

            startFragment(mArgument);

        }
        ///https://www.programcreek.com/java-api-examples/?class=android.support.v7.widget.Toolbar&method=setNavigationOnClickListener
        Log.d(TAG, "10 IngredActivity mRecipeName->  " + mRecipeName);


        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(findViewById(R.id.video_view_container) == null) {
                    if (fragmentManager.getBackStackEntryCount() > 1){
                        fragmentManager.popBackStack(BACKSTACK_INGRED_FRAG, 0);
                    } else {
                        if (fragmentManager.getBackStackEntryCount() > 0 ) {
                            finish();
                        }
                    }
                } else {
                    finish();
                }
            }
        });


    }

    //----------------------------------------------------------------------------------------------
    private boolean isTwoPane(){
        boolean twoPane;
        mLayout_Config  = getResources().getString(R.string.layout_config);
        switch (mLayout_Config) {
            case "portrait":
                twoPane = false;
                break;
            case "landscape" :
                twoPane = false;
                break;
            case "tablet_landscape":
                twoPane = true;
                break;
            case "tablet_portrait":
                twoPane = true;
                break;
            default:
                twoPane = false;
        }
        return twoPane;
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onResume(){
        super.onResume();
        getSupportActionBar().setTitle(mRecipeName);
    }

    //----------------------------------------------------------------------------------------------
    void startFragment(Bundle arg) {


        if (isTwoPane()){
            mIngredFragment.setArguments(arg);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container,  mIngredFragment, "ingredFragment")
                    .addToBackStack(BACKSTACK_INGRED_FRAG)
                    .commit();


            mVideoFragment.setArguments(arg);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.video_view_container, mVideoFragment, "videoFragment")
                    .addToBackStack(BACKSTACK_VIDEO_FRAG)
                    .commit();

        } else {
            mVideoFragment.setArguments(arg);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, mVideoFragment, "videoFragment")
                    .addToBackStack(BACKSTACK_VIDEO_FRAG)
                    .commit();
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onItemClick(int selectedIndex) {

        mSelectedStep = selectedIndex;

        getSupportActionBar().setTitle(mRecipeName);

        Bundle arg = new Bundle();
        arg.putParcelable(RecipeListActivity.ARG_RECIPE, mRecipe);
        arg.putInt(RecipeListActivity.ARG_SELECTED_STEP, mSelectedStep);

        startFragment(arg);
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------


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
        outState.putParcelable(RecipeListActivity.ARG_RECIPE, mRecipe);
        outState.putString(RecipeListActivity.ARG_RECIPE_NAME, mRecipe.getName());
        outState.putInt(RecipeListActivity.ARG_SELECTED_STEP, mSelectedStep);
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.video_view_container) == null) {
            if (fragmentManager.getBackStackEntryCount() > 1){
                fragmentManager.popBackStack(BACKSTACK_INGRED_FRAG, 0);
            } else {
                if (fragmentManager.getBackStackEntryCount() > 0 ) {
                    finish();
                }
            }
        } else {
            finish();
        }
        //super.onBackPressed();  // optional depending on your needs
    }
}
