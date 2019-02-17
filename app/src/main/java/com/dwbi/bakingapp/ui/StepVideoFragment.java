package com.dwbi.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwbi.bakingapp.R;
import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

//TODO:https://stackoverflow.com/questions/51694166/exoplayer-place-controller-under-the-video-without-overlapping-the-video
public class StepVideoFragment extends Fragment  implements View.OnClickListener {
    private static final String TAG = "STEPVI8";

    private Recipe mRecipe;
    private String mRecipeName;
    private int mSelectedStep;

    private ArrayList<Step> mSteps = new ArrayList<>();


    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private Long mPlayerPosition = Long.valueOf(0);

    String mLayout_Config;
    Boolean mTwoPane;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail_with_video, container, false);
        mLayout_Config  = getResources().getString(R.string.layout_config);



        if(savedInstanceState != null){
            mSteps = savedInstanceState.getParcelableArrayList(RecipeListActivity.ARG_STEPS);
            mSelectedStep = savedInstanceState.getInt(RecipeListActivity.ARG_SELECTED_STEP, 0);
            mRecipeName = savedInstanceState.getString(RecipeListActivity.ARG_RECIPE_NAME);

            mPlayerPosition =  savedInstanceState.getLong("playerPosition");

        } else {

            if (getArguments() != null && getArguments().containsKey(RecipeListActivity.ARG_RECIPE)) {
                Bundle argument = getArguments();
                mRecipe = argument.getParcelable(RecipeListActivity.ARG_RECIPE);
                mSelectedStep = argument.getInt(RecipeListActivity.ARG_SELECTED_STEP, 0);
                mRecipeName = argument.getString(RecipeListActivity.ARG_RECIPE_NAME);
                mSteps = mRecipe.getSteps();
                mPlayerPosition = Long.valueOf(0);
            } else {
                mRecipe = getArguments().getParcelable(RecipeListActivity.ARG_RECIPE);
                mSteps = mRecipe.getSteps();
                mSelectedStep = 0;
            }
        }


        if (mSteps != null) {
            showStep(rootView, mSelectedStep);
            rootView.findViewById(R.id.btnNextStep).setOnClickListener(this);
            rootView.findViewById(R.id.btnPrevStep).setOnClickListener(this);
            rootView.findViewById(R.id.btnRepeat).setOnClickListener(this);
        }
        return rootView;
    }

    //----------------------------------------------------------------------------------------------

    public void showStep(View rootView, Integer stepNumber) {
        ((TextView) rootView.findViewById(R.id.tvShortDescription)).setText(mSteps.get(stepNumber).getShortDescription());
        ((TextView) rootView.findViewById(R.id.tvDescription)).setText(mSteps.get(stepNumber).getDescription());

        if(mSteps.get(stepNumber).getVideoURL().isEmpty()) {
            rootView.findViewById(R.id.btnRepeat).setClickable(false);
        } else {
            rootView.findViewById(R.id.btnRepeat).setClickable(true);
        }

        String imageUrl=mSteps.get(stepNumber).getThumbnailURL();


        //if (imageUrl.isEmpty()) {
        if (!imageUrl.equals("") ) {
            Uri imageUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView thumbImage = (ImageView) rootView.findViewById(R.id.thumbImage);
            Picasso.with(getContext()).load(imageUri).into(thumbImage);
        } else {
            ImageView thumbImage = (ImageView) rootView.findViewById(R.id.thumbImage);
            Picasso.with(getContext()).load(R.drawable.icons8_microwave_filled_80).into(thumbImage);
        }

        mPlayerView = rootView.findViewById(R.id.exoView);
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        //TODO: https://medium.com/google-exoplayer/customizing-exoplayers-ui-components-728cf55ee07a
        String videoURL = mSteps.get(stepNumber).getVideoURL();

        mLayout_Config  = getResources().getString(R.string.layout_config);


        if (mExoPlayer != null){
            releasePlayer();
        }
        if( ! videoURL.isEmpty() ){
            mPlayerView.setVisibility(View.VISIBLE);
            createPlayer(Uri.parse(videoURL));
        } else {
            mExoPlayer = null;
            mPlayerView.setVisibility(View.INVISIBLE);
        }
    }


    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    //----------------------------------------------------------------------------------------------
    private void createPlayer(Uri videoUri){
        if(mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

            mExoPlayer.prepare(mediaSource);
            if(mPlayerPosition != 0) {
                mExoPlayer.seekTo(mPlayerPosition);
            }
            mExoPlayer.setPlayWhenReady(true);
        }
    }
    //----------------------------------------------------------------------------------------------


    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mSteps.get(mSelectedStep).getVideoURL())) {
            if (mExoPlayer != null){
                releasePlayer();
            }
            createPlayer(Uri.parse(mSteps.get(mSelectedStep).getVideoURL()));
        }
    }
    //----------------------------------------------------------------------------------------------




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RecipeListActivity.ARG_STEPS, mSteps);
        outState.putInt(RecipeListActivity.ARG_SELECTED_STEP, mSelectedStep);
        outState.putString(RecipeListActivity.ARG_RECIPE_NAME, mRecipeName);
        if(mExoPlayer != null) {
            outState.putLong("playerPosition", mExoPlayer.getCurrentPosition());

        } else {
            outState.putLong("playerPosition", Long.valueOf(0));
        }
    }





    @Override
    public void onDetach()
    {
        super.onDetach();
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNextStep:
                if(mSelectedStep < mSteps.size()-1) {
                    ++mSelectedStep;
                    showStep(v.getRootView(), mSelectedStep);
                }
                break;
            case R.id.btnPrevStep:
                if(mSelectedStep > 0) {
                    --mSelectedStep;
                    showStep(v.getRootView(), mSelectedStep);
                }
                break;
            case R.id.btnRepeat:
                if (mExoPlayer != null) {
                    mExoPlayer.seekTo(0);
                }
                break;
            default:
                break;
        }
    }

}
