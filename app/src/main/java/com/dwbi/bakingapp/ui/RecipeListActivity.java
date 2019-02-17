package com.dwbi.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.dwbi.bakingapp.IdlingResource.SimpleIdlingResource;
import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.R;
import com.dwbi.bakingapp.adapter.RecipeListAdapter;
import com.dwbi.bakingapp.network.RecipeAPI;
import com.dwbi.bakingapp.network.RecipeService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.onClickListener {

    public static final String ARG_RECIPE = "recipe";
    public static final String ARG_RECIPE_NAME = "recipename";
    public static final String ARG_BUNDLE = "bundle";
    public static final String ARG_STEPS = "steps";
    public static final String ARG_SELECTED_STEP = "selected_step";

    private SimpleIdlingResource mIdlingResource;
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static final String TAG = "PSX";
    private boolean mTwoPane;


    RecipeAPI client;
    ArrayList<Recipe> mRecipeList = new ArrayList<>();
    String mLayout_Config;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipelistactivity_layout);
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Udacity Baking App");

        mLayout_Config  = getResources().getString(R.string.layout_config);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_recipelist);
        assert recyclerView != null;



        // from getRecipes fills the recyclerview
        getRecipes(recyclerView);

        getIdlingResource();
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        //recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        switch (mLayout_Config) {
            case "portrait":
                mTwoPane = false;
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                //recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case "landscape" :
                mTwoPane = false;
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case "tablet_landscape":
                mTwoPane = true;
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            default:
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        recyclerView.setAdapter(new RecipeListAdapter(this, mRecipeList, mTwoPane));
    }

    //----------------------------------------------------------------------------------------------

    private void getRecipes(final RecyclerView recyclerView){
        final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";



        if(client == null){
            client = RecipeService.createRecipeService(RecipeAPI.class);
        }

        Log.d(TAG, "RecipeListActivity-> client.getRecipes();");

        Call<ArrayList<Recipe>> call = client.getRecipes();


        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> result = response.body();

                mRecipeList = result;
                setupRecyclerView(recyclerView);

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }

            }
    
            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                mRecipeList = new ArrayList<>();
            }
        });

    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onItemClick(int selectedIndex) {
        Bundle arg = new Bundle();
        arg.putParcelable(RecipeListActivity.ARG_RECIPE, mRecipeList.get(selectedIndex));
        Context context = this;
        Intent intent = new Intent(context, IngredActivity.class);
        intent.putExtra(RecipeListActivity.ARG_BUNDLE, arg);
        context.startActivity(intent);
    }
    //----------------------------------------------------------------------------------------------



}
