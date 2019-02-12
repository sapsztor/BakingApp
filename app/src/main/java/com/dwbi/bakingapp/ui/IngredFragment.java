package com.dwbi.bakingapp.ui;

import android.os.Bundle;
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
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    public static final String ARG_ITEM_ID = "recipe";

    private static final String TAG = "PSX";


    private String mItem;
    private Recipe mRecipe;
    TextView mIngredients;
    private RecyclerView rvSteps;

    String mLayout_Config;
    Boolean mTwoPane;



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

        //mItem = getActivity().getIntent().getExtras().getString(IngredFragment.ARG_ITEM_ID);
        if (getArguments() != null && getArguments().containsKey(IngredFragment.ARG_ITEM_ID)) {
            //mItem = getArguments().getString(IngredFragment.ARG_ITEM_ID);
            mRecipe = getArguments().getParcelable(IngredFragment.ARG_ITEM_ID);
        }

        if (mRecipe != null) {

            //dumpRecipe(mRecipe);

            // ingredients
            List<Ingredient> ingreds = mRecipe.getIngredients();

            String ingredtext = "";
            for(Ingredient i : ingreds) {
                //ingredtext.concat(i.getIngredient() + " " + i.getQuantity() + " " + i.getMeasure()+ "\n");
                ingredtext = ingredtext + " &#8226 " + i.getIngredient() + " <b>" + i.getQuantity() + " " + i.getMeasure()+ "</b><br>";
                //ingredtext = ingredtext + "&#9830  " + i.getIngredient() + " <b>" + i.getQuantity() + " " + i.getMeasure()+ "</b><br>";
            }

            ((TextView) rootView.findViewById(R.id.tvIngredients)).setText(Html.fromHtml(ingredtext));
            ArrayList<Step> steps = new ArrayList<Step>(mRecipe.getSteps());

            rvSteps = rootView.findViewById(R.id.rv_recipe_steps);
            rvSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvSteps.setAdapter(new StepsAdapter( steps, (IngredActivity) getActivity(), checkTwoPane()));
        }


        return rootView;
    }

    //----------------------------------------------------------------------------------------------
    public void dumpRecipe(Recipe recipe) {
        Log.d(TAG, "-------------------------------------------------");
        Log.d(TAG, "Recipe-> " + recipe.getName());
        Log.d(TAG, "Image-> " + recipe.getImage());
        Log.d(TAG, "Servings-> " + recipe.getServings());
        for(Ingredient i: recipe.getIngredients()){
            Log.d(TAG, "\tIngredient \t" + i.getIngredient() + " : " + i.getQuantity()  + ", " + i.getMeasure());
        }
        for(Step s: recipe.getSteps()){
            Log.d(TAG, "\tStep \t" + s.getId() + " : " + s.getShortDescription() + " : " + s.getThumbnailURL()  + ", " + s.getVideoURL());
        }
        Log.d(TAG, "-------------------------------------------------");
    }
    //----------------------------------------------------------------------------------------------

}
