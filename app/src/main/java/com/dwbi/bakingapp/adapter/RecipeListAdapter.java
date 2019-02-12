package com.dwbi.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.ui.IngredActivity;
import com.dwbi.bakingapp.ui.IngredFragment;
import com.dwbi.bakingapp.ui.RecipeListActivity;
import com.dwbi.bakingapp.R;
import com.dwbi.bakingapp.ui.PlaceHolderImages;
import com.dwbi.bakingapp.widget.WidgetUpdateService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private static final String TAG = "PSX";

    final private onClickListener onClickListener;

    public interface onClickListener {
        void onItemClick(Recipe recipe);
    }

    Context mContext;

    private final RecipeListActivity mParentActivity;
    private final ArrayList<Recipe> mRecipes;


    private final boolean mTwoPane;


    public RecipeListAdapter(RecipeListActivity parent, ArrayList<Recipe> items, boolean twoPane) {
        mRecipes = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
        onClickListener = parent;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recipe_cardlist_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mName.setText(mRecipes.get(position).getName());


        String imagePath = mRecipes.get(position).getImage().isEmpty() ? PlaceHolderImages.getPlaceHolderImagePath(position) : mRecipes.get(position).getImage() ;

        String placeholderPath = PlaceHolderImages.getPlaceHolderImagePath(position);

        Picasso.with(holder.mImage.getContext()).setIndicatorsEnabled(false);
        Picasso.with(holder.mImage.getContext())
                .load(imagePath)
                .placeholder(R.drawable.pexels_photo_558477)
                .error(R.drawable.pexels_photo_558477)
                //.resize(1800, 600)
                //.centerInside()
                .fit()
                .centerCrop()
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
    //------------------------------------------------------------------------------------------
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName;
        TextView mServings;
        ImageView mImage;
        TextView mIngredients;

        ViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.tvRecipeName);
            mServings = view.findViewById(R.id.tvRecipeServings);
            mImage = view.findViewById(R.id.ivRecipeImage);
            mIngredients = view.findViewById(R.id.tvIngredients);

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "onClick position -> " + position);
            Recipe recipe = mRecipes.get(position);


            updateWidget(v.getContext(), recipe);

            onClickListener.onItemClick(recipe);

        }

        void updateWidget(Context context, Recipe recipe) {
            WidgetUpdateService.startWidgetUpdateService(context, recipe);
        }
    }
    //------------------------------------------------------------------------------------------
}