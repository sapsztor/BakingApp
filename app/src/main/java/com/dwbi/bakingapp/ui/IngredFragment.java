package com.dwbi.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwbi.bakingapp.model.Ingredient;
import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.R;
import com.dwbi.bakingapp.adapter.StepsAdapter;
import com.dwbi.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link IngredActivity}
 * on handsets.
 */
public class IngredFragment extends Fragment {
    /**
     * The fragment mArgument representing the item ID that this fragment
     * represents.
     */

    //public static final String ARG_RECIPE = "recipe";

    private static final String TAG = "PSX";


    private Recipe mRecipe;
    private RecyclerView rvSteps;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredFragment() {
    }

    public Boolean checkTwoPane(){
        String layout_config = getResources().getString(R.string.layout_config);
        boolean istwopane;

        switch (layout_config) {
            case "portrait":
                istwopane = false;
                break;
            case "landscape" :
                istwopane = false;
                break;
            case "tablet_landscape":
                istwopane = true;
                break;
            default:
                istwopane = false;
        }
        Log.d(TAG, "IngredFragment-> layout_config-> " + layout_config);
        return istwopane;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if(savedInstanceState != null ) {
            mRecipe = savedInstanceState.getParcelable(RecipeListActivity.ARG_RECIPE);
        } else {
            if (getArguments() != null && getArguments().containsKey(RecipeListActivity.ARG_RECIPE)) {
                Bundle argument = getArguments();
                mRecipe = argument.getParcelable(RecipeListActivity.ARG_RECIPE);
            }
        }


        if (mRecipe != null) {
            // ingredients
            ArrayList<Ingredient> ingreds = mRecipe.getIngredients();

            String ingredtext = "";
            for(Ingredient i : ingreds) {
                ingredtext = ingredtext + " &#8226 " + i.getIngredient() + " <b>" + i.getQuantity() + " " + i.getMeasure()+ "</b><br>";
            }

            ((TextView) rootView.findViewById(R.id.tvIngredients)).setText(Html.fromHtml(ingredtext));
            ArrayList<Step> steps = new ArrayList<Step>(mRecipe.getSteps());

            rvSteps = rootView.findViewById(R.id.rv_recipe_steps);
            rvSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvSteps.setAdapter(new StepsAdapter( steps, (IngredActivity) getActivity(), checkTwoPane()));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeListActivity.ARG_RECIPE, mRecipe);
    }
}
