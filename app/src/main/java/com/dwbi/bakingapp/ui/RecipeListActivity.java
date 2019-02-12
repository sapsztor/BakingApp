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
import com.dwbi.bakingapp.model.Ingredient;
import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.R;
import com.dwbi.bakingapp.adapter.RecipeListAdapter;
import com.dwbi.bakingapp.model.Step;
import com.dwbi.bakingapp.network.RecipeAPI;
import com.dwbi.bakingapp.network.RecipeService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link IngredActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.onClickListener {


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
    ArrayList<Recipe> mData = new ArrayList<>();
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
        recyclerView.setAdapter(new RecipeListAdapter(this, mData, mTwoPane));
    }

    //----------------------------------------------------------------------------------------------

    private void getRecipes(final RecyclerView recyclerView){
        final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";



        if(client == null){
            client = RecipeService.createRecipeService(RecipeAPI.class);
        }
        Call<ArrayList<Recipe>> call = client.getRecipes();

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> result = response.body();

                mData = result;
                setupRecyclerView(recyclerView);
            }
    
            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                mData = new ArrayList<>();
            }
        });

    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onItemClick(Recipe recipe) {
        if(mTwoPane) {

            Log.d(TAG, "Detail fragment inditas");
            Bundle arg = new Bundle();
            arg.putParcelable(IngredFragment.ARG_ITEM_ID, recipe);

            Context context = this;
            Intent intent = new Intent(context, IngredActivity.class);
            intent.putExtra("recipe", arg);
            context.startActivity(intent);


        } else {
            Log.d(TAG, "Detail activity inditas");
            Bundle arg = new Bundle();
            arg.putParcelable(IngredFragment.ARG_ITEM_ID, recipe);

            Context context = this;
            Intent intent = new Intent(context, IngredActivity.class);
            intent.putExtra("recipe", arg);
            context.startActivity(intent);
        }
    }
    //----------------------------------------------------------------------------------------------
    
}
