package com.dwbi.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwbi.bakingapp.R;
import com.dwbi.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
//TODO:https://stackoverflow.com/questions/51694166/exoplayer-place-controller-under-the-video-without-overlapping-the-video
public class StepVideoFragment extends Fragment  implements View.OnClickListener {
    private static final String TAG = "PSX";

    Bundle argument;
    private ArrayList<Step> mSteps;
    private int mSelectedStep;
    private String mRecipeName;



    public static final String ARG_ITEM_ID = "steps";
    public static final String ARG_SELECTED_STEP = "selected_step";
    public static final String ARG_RECIPE_NAME = "recipe_name";


    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;

    String mLayout_Config;
    Boolean mTwoPane;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
            mSteps = savedInstanceState.getParcelableArrayList(StepVideoFragment.ARG_ITEM_ID);
            mSelectedStep = savedInstanceState.getInt(StepVideoFragment.ARG_SELECTED_STEP, 0);
            mRecipeName = savedInstanceState.getString(StepVideoFragment.ARG_RECIPE_NAME);

        } else {
            if (getArguments().containsKey(StepVideoFragment.ARG_ITEM_ID)) {
                mSteps = getArguments().getParcelableArrayList(StepVideoFragment.ARG_ITEM_ID);
                mSelectedStep = getArguments().getInt(StepVideoFragment.ARG_SELECTED_STEP, 0);
                mRecipeName = getArguments().getString(StepVideoFragment.ARG_RECIPE_NAME);

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



    public void showStep(View rootView, Integer stepNumber) {

        ((TextView) rootView.findViewById(R.id.tvShortDescription)).setText(mSteps.get(stepNumber).getShortDescription());
        ((TextView) rootView.findViewById(R.id.tvDescription)).setText(mSteps.get(stepNumber).getDescription());

        mPlayerView = rootView.findViewById(R.id.exoView);
        //TODO: https://medium.com/google-exoplayer/customizing-exoplayers-ui-components-728cf55ee07a
        String videoURL = mSteps.get(stepNumber).getVideoURL();

        if (mExoPlayer != null){
            releasePlayer();
        }
        if( ! videoURL.isEmpty() ){
            createPlayer(Uri.parse(videoURL));
        }

    }

    private void createPlayer(Uri videoUri){
        if(mExoPlayer == null) {


            //mHandler = new Handler();
            TrackSelector trackSelector = new DefaultTrackSelector();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "masterdetail");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
            //TODO: https://google.github.io/ExoPlayer/guide.html
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(StepVideoFragment.ARG_ITEM_ID, mSteps);
        outState.putInt(StepVideoFragment.ARG_SELECTED_STEP, mSelectedStep);
        outState.putString(StepVideoFragment.ARG_RECIPE_NAME, "Sajat recept");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mExoPlayer != null){
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mExoPlayer != null){
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null){
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null){
            releasePlayer();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "StepVideoFragment-------------------> home button pressed");
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
                mExoPlayer.seekTo(0);
                break;
            default:
                break;
        }
    }

}
