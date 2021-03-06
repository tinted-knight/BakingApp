package com.and.tim.bakingapp.ui.step_instrunctions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.dao.entities.StepEntity;
import com.and.tim.bakingapp.test.EspressoIdlingResources;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import icepick.Icepick;
import icepick.State;

public class StepInstructionsFragment extends Fragment {

    private StepInstructionsVM viewModel;
    private boolean modeMobileLandscape = false;
    private boolean modeTablet = false;

    //Bind
    @Nullable @BindView(R.id.tvShortDescription) TextView tvShortDescription;
    @Nullable @BindView(R.id.tvDescription) TextView tvDescription;
    @Nullable @BindView(R.id.tvStepCount) TextView tvStepCount;
    @Nullable @BindView(R.id.btnNext) Button btnNext;
    @Nullable @BindView(R.id.btnPrev) Button btnPrevious;
    @BindView(R.id.ivNoVideo) ImageView ivNoVideo;

    @BindView(R.id.exoPlayer) PlayerView playerView;
    private SimpleExoPlayer player;

    @State long playbackPosition;
    @State int currentWindow;
    @State boolean playWhenReady;

    public StepInstructionsFragment() {
        // Required empty public constructor
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        } else {
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

        EspressoIdlingResources.increment();

        if (root.findViewById(R.id.layoutMobileLandscape) != null) {
            modeMobileLandscape = true;
            hideSystemUi();
        } else if (root.findViewById(R.id.layoutTablet) != null) {
            modeTablet = true;
        }

        return root;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(StepInstructionsVM.class);
        registerObservers();
    }

    private void initExoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        playerView.setPlayer(player);
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

    private Observer<StepEntity> mobileLandscapeObserver = new Observer<StepEntity>() {
        @Override public void onChanged(@Nullable StepEntity step) {
            if (step != null) {
                showStepVideo(step.getVideoURL());
            }
        }
    };

    private Observer<StepEntity> commonObserver = new Observer<StepEntity>() {
        @Override public void onChanged(@Nullable StepEntity step) {
            if (step != null) {
                showStepText(step);
                showStepVideo(step.getVideoURL());
            }
        }
    };

    private void registerObservers() {
        Observer<StepEntity> stepDataObserver;
        if (modeMobileLandscape) {
            stepDataObserver = mobileLandscapeObserver;
        } else {
            stepDataObserver = commonObserver;
            stepNavigationObserve();
        }
        viewModel.getStepData().observe(this, stepDataObserver);
        actionBatTitleObserve();
    }

    private void stepNavigationObserve() {
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

    private void actionBatTitleObserve() {
        viewModel.getRecipeName().observe(this, new Observer<String>() {
            @Override public void onChanged(@Nullable String value) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(value);
            }
        });
    }

    @Optional @OnClick({R.id.btnNext, R.id.btnPrev})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                EspressoIdlingResources.increment();
                viewModel.doNextStep();
                break;
            case R.id.btnPrev:
                EspressoIdlingResources.increment();
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
            ivNoVideo.setVisibility(View.GONE);
        } else {
            playerView.setVisibility(View.INVISIBLE);
            player.prepare(null);
            ivNoVideo.setVisibility(View.VISIBLE);
        }
        EspressoIdlingResources.decrement();
    }

    private void showStepText(StepEntity step) {
        if (!modeMobileLandscape) {
            tvShortDescription.setText(step.getShortDescription());
            tvDescription.setText(step.getDescription());
        }
    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override public void onStart() {
        super.onStart();
        if (player == null) {
            initExoPlayer();
        }
    }

    @Override public void onResume() {
        super.onResume();
        if (player == null) {
            initExoPlayer();
        }
    }

    @Override public void onPause() {
        super.onPause();
        if (player != null) {
            releaseExoPlayer();
        }
    }

    @Override public void onStop() {
        super.onStop();
        if (player != null) {
            releaseExoPlayer();
        }
    }
}
