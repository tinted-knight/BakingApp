package com.and.tim.bakingapp.ui.step_instrunctions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.dao.StepEntity;
import com.and.tim.bakingapp.viewmodel.StepInstructionsVM;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import icepick.Icepick;
import icepick.State;

public class StepInstructionsFragment extends Fragment {

    private static final String KEY_RECIPE = "recipe_key";
    private static final String KEY_STEP = "step_key";

    private int recipeId;
    private int stepId;
    private StepInstructionsVM viewModel;
    private boolean modeMobileLandscape = false;
    private boolean modeTablet = false;

    //Bind
    @Nullable @BindView(R.id.tvShortDescription) TextView tvShortDescription;
    @Nullable @BindView(R.id.tvDescription) TextView tvDescription;
    @Nullable @BindView(R.id.tvStepCount) TextView tvStepCount;
    @Nullable @BindView(R.id.btnNext) Button btnNext;
    @Nullable @BindView(R.id.btnPrev) Button btnPrevious;

    @BindView(R.id.exoPlayer) PlayerView playerView;
    private SimpleExoPlayer player;

    @State long playbackPosition;
    @State int currentWindow;
    @State boolean playWhenReady;

    public StepInstructionsFragment() {
        // Required empty public constructor
    }

    public static StepInstructionsFragment newInstance(int recipeId, int stepId) {
        StepInstructionsFragment fragment = new StepInstructionsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_RECIPE, recipeId);
        args.putInt(KEY_STEP, stepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        } else if (getArguments() != null) {
            recipeId = getArguments().getInt(KEY_RECIPE);
            stepId = getArguments().getInt(KEY_STEP);
            playbackPosition = 0;
            currentWindow = 0;
            playWhenReady = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step_instructions, container, false);
        ButterKnife.bind(this, root);

        if (root.findViewById(R.id.layoutMobileLandscape) != null) {
            modeMobileLandscape = true;
        } else if (root.findViewById(R.id.layoutTablet) != null) {
            modeTablet = true;
        }

        return root;
    }

    private void initExoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        playerView.setPlayer(player);
        playerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_video));
        player.seekTo(currentWindow, playbackPosition);
        player.setPlayWhenReady(playWhenReady);
    }

    private void releaseExoPlayer() {
        if (player != null) {
            playbackPosition = player.getContentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(StepInstructionsVM.class);
//        viewModel.setStep(stepId);
        if (tvShortDescription != null)
            registerObservers();
        else
            registerLandscapeObservers();
    }

    private void registerLandscapeObservers() {
        viewModel.getStepData().observe(this, new Observer<StepEntity>() {
            @Override public void onChanged(@Nullable StepEntity step) {
                if (step != null) {
                    showStepVideo(step.getVideoURL());
                }
            }
        });
    }

    private void registerObservers() {
        viewModel.getStepData().observe(this, new Observer<StepEntity>() {
            @Override public void onChanged(@Nullable StepEntity step) {
                if (step != null) {
                    showStepText(step);
                    showStepVideo(step.getVideoURL());
                }
            }
        });
        viewModel.getHasNextStep().observe(this, new Observer<Boolean>() {
            @Override public void onChanged(@Nullable Boolean value) {
                btnNext.setEnabled(value != null && value);
            }
        });
        viewModel.getHasPreviousStep().observe(this, new Observer<Boolean>() {
            @Override public void onChanged(@Nullable Boolean value) {
                btnPrevious.setEnabled(value != null && value);
            }
        });
        viewModel.getStepCounter().observe(this, new Observer<Pair<Integer, Integer>>() {
            @Override public void onChanged(@Nullable Pair<Integer, Integer> pair) {
                String message = "...";
                if (pair != null)
                    message = "Step " + pair.first + " of " + pair.second;
                tvStepCount.setText(message);
            }
        });
    }

    @Optional @OnClick({R.id.btnNext, R.id.btnPrev})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                viewModel.doNextStep();
                break;
            case R.id.btnPrev:
                viewModel.doPreviousStep();
                break;
        }
    }

    private void showStepVideo(String videoURLString) {
        if (!"".equals(videoURLString.trim())) {
            Uri uri = Uri.parse(videoURLString);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("exoplayer-bakingapp")).createMediaSource(uri);
            playerView.setVisibility(View.VISIBLE);
            player.prepare(mediaSource, true, false);
            player.seekTo(currentWindow, playbackPosition);
        } else {
            playerView.setVisibility(View.INVISIBLE);
            player.prepare(null);
            Toast.makeText(getActivity(), "no video :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void showStepText(StepEntity step) {
        if (modeMobileLandscape) {
//            hideSystemUi();
        } else {
            tvShortDescription.setText(step.getShortDescription());
            tvDescription.setText(step.getDescription());
        }
    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override public void onStart() {
        super.onStart();
//        if (Util.SDK_INT > 23) {
        initExoPlayer();
//        }
    }

    @Override public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 && player == null) {
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
