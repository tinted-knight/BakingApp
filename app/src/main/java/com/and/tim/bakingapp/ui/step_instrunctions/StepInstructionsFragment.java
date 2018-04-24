package com.and.tim.bakingapp.ui.step_instrunctions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.model.Step;
import com.and.tim.bakingapp.repo.dao.StepEntity;
import com.and.tim.bakingapp.viewmodel.StepInstructionsViewModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class StepInstructionsFragment extends Fragment {

    private static final String ARG_PARAM1 = "recipe_key";
    private static final String ARG_PARAM2 = "step_key";

    private int recipeId;
    private int stepId;
    private StepInstructionsViewModel viewModel;
    private boolean modeMobileLandscape = false;

    //Bind
    @Nullable
    @BindView(R.id.tvShortDescription)
    TextView tvShortDescription;
    @BindView(R.id.exoPlayer)
    PlayerView playerView;
    private SimpleExoPlayer player;

    public StepInstructionsFragment() {
        // Required empty public constructor
    }

    public static StepInstructionsFragment newInstance(int recipeId, int stepId) {
        StepInstructionsFragment fragment = new StepInstructionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, recipeId);
        args.putInt(ARG_PARAM2, stepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step_instructions, container, false);
        ButterKnife.bind(this, root);

        if (root.findViewById(R.id.layoutMobileLandscape) != null) {
            modeMobileLandscape = true;
        }

        return root;
    }

    private void initExoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.seekTo(0, 0);

//        Uri uri = Uri.parse("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
//        MediaSource mediaSource = new ExtractorMediaSource.Factory(
//                new DefaultHttpDataSourceFactory("exoplayer-bakingapp")).createMediaSource(uri);
//        player.prepare(mediaSource, true, false);
    }

    private void releaseExoPlayer() {
        if (player != null) {
            long playbackPosition = player.getContentPosition();
            int currentWindow = player.getCurrentWindowIndex();
            boolean playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            recipeId = getArguments().getInt(ARG_PARAM1);
//            stepId = getArguments().getInt(ARG_PARAM2);
//        }
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(StepInstructionsViewModel.class);
//        viewModel.init(recipeId, stepId);
        registerObservers();
    }

    private void registerObservers() {
        viewModel.stepData.observe(this, new Observer<StepEntity>() {
            @Override public void onChanged(@Nullable StepEntity step) {
                if (step != null) {
                    showStepText(step);
                    viewModel.setStepId(step._id);
                    showStepVideo(step.videoURL);
                }
            }
        });
    }

    private void showStepVideo(String videoURL) {
        if (!"".equals(videoURL.trim())) {
            Uri uri = Uri.parse(videoURL);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("exoplayer-bakingapp")).createMediaSource(uri);
            player.prepare(mediaSource, true, false);
        } else {
            Toast.makeText(getActivity(), "no video :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void showStepText(StepEntity step) {
        if (modeMobileLandscape) {
            // nothing for a while
        } else { // Mode is mobile portrait
            tvShortDescription.setText(step.shortDescription);
        }
    }

    @Override public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initExoPlayer();
        }
    }

    @Override public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initExoPlayer();
        }
    }

    @Override public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releaseExoPlayer();
        }
    }

    @Override public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseExoPlayer();
        }
    }
}
