package com.dwbi.bakingapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwbi.bakingapp.ui.IngredActivity;
import com.dwbi.bakingapp.ui.StepVideoFragment;
import com.dwbi.bakingapp.R;
import com.dwbi.bakingapp.model.Step;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private static final String TAG = "PSX";

    Context mContext;
    private final IngredActivity mParent ;

    private final ArrayList<Step> mSteps;

    private final boolean mTwoPane;

    final private onClickListener onClickListener;

    public interface onClickListener {
        void onItemClick(int selectedIndex);
    }

    public StepsAdapter(ArrayList<Step> items, IngredActivity parent, boolean twoPane) {
        mSteps = items;
        mParent = parent;
        onClickListener = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_detail_item_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvStep.setText(position + " " + mSteps.get(position).getShortDescription());


    }

    @Override
    public int getItemCount() {

        return  mSteps.size();
    }




    //------------------------------------------------------------------------------------------
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvStep;

        ViewHolder(View view) {
            super(view);
            tvStep = view.findViewById(R.id.tvStep);

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            onClickListener.onItemClick(position);

//            Step step = mSteps.get(position);
//            Bundle arg = new Bundle();
//
//            //arg.putParcelable(StepVideoFragment.ARG_RECIPE, mSteps);
//            arg.putParcelableArrayList(StepVideoFragment.ARG_RECIPE, mSteps);
//            arg.putInt(StepVideoFragment.ARG_SELECTED_STEP, position);
//            arg.putString(StepVideoFragment.ARG_RECIPE_NAME, "Sajat recept");
//
//            StepVideoFragment videoFragment = new StepVideoFragment();
//            videoFragment.setArguments(arg);
//
//
//            if(mTwoPane) {
//
//                mParent.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.video_view_container, videoFragment)
//                        //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
//                        //.addToBackStack(null)
//                        .commit();
//
//            } else {
//
//                mParent.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.item_detail_container, videoFragment)
//                        //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
//                        .addToBackStack(null)
//                        .commit();
//            }
        }
    }
    //------------------------------------------------------------------------------------------

}
