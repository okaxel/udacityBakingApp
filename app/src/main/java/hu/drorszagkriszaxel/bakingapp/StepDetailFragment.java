package hu.drorszagkriszaxel.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import hu.drorszagkriszaxel.bakingapp.model.StepsItem;

/**
 *
 * Baking App component.
 *
 * Copyright © 2018 by Axel Ország-Krisz Dr.
 *
 * @author Axel Ország-Krisz Dr.
 * @version 1.0 - first try
 *
 * ---
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---
 *
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 */
public class StepDetailFragment extends Fragment {

    private static final byte FULLSCREEN_NOT_DETECTED_AND_NEVER_CHANGED = 0;
    private static final byte FULLSCREEN_OFF = 1;
    private static final byte FULLSCREEN_ON = 2;

    @BindView(R.id.step_player) PlayerView playerView;
    @BindView(R.id.step_image) ImageView imageView;
    @BindView(R.id.step_detail) TextView tvDetails;
    @BindView(R.id.step_dummy) TextView dummyView;
    @BindView(R.id.step_detail_card) CardView stepDetailCard;

    private StepsItem stepsItem;
    private boolean singlePaneMode;

    private SimpleExoPlayer simpleExoPlayer;
    private long playerPosition;
    private boolean playerWhenReady;
    private String playedLink;
    private byte exoFullscreen;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    /**
     * Usual fragment creator.
     *
     * @param savedInstanceState State if saved
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(getString(R.string.key_exo_position))) {

                playerPosition = savedInstanceState.getLong(getString(R.string.key_exo_position));

            } else {

                playerPosition = 0;

            }

            playerWhenReady = !savedInstanceState.containsKey(getString(R.string.key_exo_state)) || savedInstanceState.getBoolean(getString(R.string.key_exo_state));

            if (savedInstanceState.containsKey(getString(R.string.key_exo_link))) {

                playedLink = savedInstanceState.getString(getString(R.string.key_exo_link));

            } else {

                playedLink = null;

            }

            if (savedInstanceState.containsKey(getString(R.string.key_exo_fullscreen))) {

                exoFullscreen = savedInstanceState.getByte(getString(R.string.key_exo_fullscreen));

            } else exoFullscreen = FULLSCREEN_NOT_DETECTED_AND_NEVER_CHANGED;

        } else {

            playerPosition = 0;
            playerWhenReady = true;
            playedLink = null;
            exoFullscreen = FULLSCREEN_NOT_DETECTED_AND_NEVER_CHANGED;

        }

        if (getArguments()!= null) {

            if (getArguments().containsKey(getString(R.string.key_step_item))) {

                stepsItem = getArguments().getParcelable(getString(R.string.key_step_item));

            }

            if (getArguments().containsKey(getString(R.string.key_fragment_single_pane_mode))) {

                singlePaneMode = getArguments().getBoolean(getString(R.string.key_fragment_single_pane_mode));

            }

        }

    }

    /**
     * Simple fragment creator which eventually initializes the fragment.
     *
     * @param inflater           The inflater instance
     * @param container          The container instance
     * @param savedInstanceState Saved instances if any
     * @return                   The View that contains everything needed
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this,rootView);

        tvDetails.setText(stepsItem.getDescription());

        Boolean videoFromThumbnail = false;

        if (stepsItem.getThumbnailURL() != null && !stepsItem.getThumbnailURL().equals("")) {

            String mimeType = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(stepsItem.getThumbnailURL());
            if (extension != null) {

                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

            }

            if (mimeType!= null) {

                if (mimeType.contains("image")) {

                    Picasso.get().load(stepsItem.getThumbnailURL()).into(imageView);
                    dummyView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                } else videoFromThumbnail = mimeType.contains("video");

            }

        }

        if (videoFromThumbnail) playedLink = stepsItem.getThumbnailURL(); else {

            if (stepsItem.getVideoURL() != null && !stepsItem.getVideoURL().equals(""))
                playedLink = stepsItem.getVideoURL(); else playedLink = null;

        }

        if (playedLink !=null) initializeExoPlayer(rootView.getContext());

        return rootView;

    }

    /**
     * Simple method to save instance states. To ExoPlayer's persistence this method is very
     * important.
     *
     * @param outState A Bundle to fill
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        // Removed because we have the playerPosition in onPause yet. (Suggestion from Review)
        //
        // if (playedLink != null)
        //    playerPosition = simpleExoPlayer.getCurrentPosition(); else playerPosition = 0;

        outState.putLong(getString(R.string.key_exo_position),playerPosition);
        outState.putBoolean(getString(R.string.key_exo_state),playerWhenReady);
        outState.putString(getString(R.string.key_exo_link),playedLink);
        outState.putByte(getString(R.string.key_exo_fullscreen),exoFullscreen);
        super.onSaveInstanceState(outState);

    }

    /**
     * If the user doesn't use the activity they don't need ExoPlayer too. This method solves it if
     * API level is less then 23. (Suggestion from Review)
     */
    @Override
    public void onPause() {

        if (playedLink != null && Util.SDK_INT <= 23) {

            playerWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.setPlayWhenReady(false);
            playerPosition = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();

            // ExoPlayer is nullified here to completely release the player. (Requirement from Review)
            simpleExoPlayer = null;

        }

        super.onPause();

    }

    /**
     * If the user doesn't use the activity they don't need ExoPlayer too. This method solves it if
     * API level is at least 23. (Suggestion from Review)
     */
    @Override
    public void onStop() {

        if (playedLink != null && Util.SDK_INT > 23) {

            playerWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.setPlayWhenReady(false);
            playerPosition = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();

            // ExoPlayer is nullified here to completely release the player. (Requirement from Review)
            simpleExoPlayer = null;

        }

        super.onStop();
    }

    /**
     * If the user uses the activity again they need ExoPlayer too. This method solves it. In case
     * if the device isn't tablet, this method initiates the toggle of fullscreen mode of the videos
     * if needed. To ensure all of this happens always this method starts when API level is less
     * then 23. (Suggestion from Review)
     */
    @Override
    public void onResume() {

        if (Util.SDK_INT <= 23) {

            // Old version. Now I think it was sort of type. See the new version below.
            // if (playerPosition !=0 ) initializeExoPlayer(getContext());

            // New version. (The base of this solution is a requirement of the Review)
            if (playedLink != null) initializeExoPlayer(getContext());

            if (singlePaneMode) {

                Configuration configuration = getResources().getConfiguration();

                if (exoFullscreen == FULLSCREEN_NOT_DETECTED_AND_NEVER_CHANGED) {

                    toggleFullscreen(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE);

                } else {

                    if (exoFullscreen == FULLSCREEN_ON) {

                        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

                            exoFullscreen = FULLSCREEN_OFF;
                            toggleFullscreen(false);

                        }

                    } else {

                        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                            exoFullscreen = FULLSCREEN_ON;
                            toggleFullscreen(true);

                        }

                    }

                }

            }

        }

        super.onResume();

    }

    /**
     * If the user uses the activity again they need ExoPlayer too. This method solves it. In case
     * if the device isn't tablet, this method initiates the toggle of fullscreen mode of the videos
     * if needed. To ensure all of this happens as soon as possible this method starts when API
     * level is at least 23. (Suggestion from Review)
     */
    @Override
    public void onStart() {

        if (Util.SDK_INT > 23) {

            // Old version. Now I think it was sort of type. See the new version below.
            // if (playerPosition !=0 ) initializeExoPlayer(getContext());

            // New version. (The base of this solution is a requirement of the Review)
            if (playedLink != null) initializeExoPlayer(getContext());

            if (singlePaneMode) {

                Configuration configuration = getResources().getConfiguration();

                if (exoFullscreen == FULLSCREEN_NOT_DETECTED_AND_NEVER_CHANGED) {

                    toggleFullscreen(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE);

                } else {

                    if (exoFullscreen == FULLSCREEN_ON) {

                        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

                            exoFullscreen = FULLSCREEN_OFF;
                            toggleFullscreen(false);

                        }

                    } else {

                        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                            exoFullscreen = FULLSCREEN_ON;
                            toggleFullscreen(true);

                        }

                    }

                }

            }

        }

        super.onStart();
    }

    /**
     * This method toggles fullscreen mode of ExoPlayer.
     *
     * @param on Mode selector
     */
    private void toggleFullscreen(boolean on) {

        StepDetailActivity stepDetailActivity = (StepDetailActivity) getActivity();

        if (stepDetailActivity != null) {

            if (playedLink != null) {

                if (on) {

                    Toast.makeText(getContext(), getString(R.string.toast_turn_from_fullscreen), Toast.LENGTH_SHORT).show();
                    stepDetailActivity.toolbar.setVisibility(View.GONE);
                    stepDetailActivity.buttonsNest.setVisibility(View.GONE);
                    stepDetailCard.setVisibility(View.GONE);
                    exoFullscreen = FULLSCREEN_ON;

                } else {

                    Toast.makeText(getContext(), getString(R.string.toast_turn_to_fullscreen), Toast.LENGTH_SHORT).show();
                    stepDetailActivity.toolbar.setVisibility(View.VISIBLE);
                    stepDetailActivity.buttonsNest.setVisibility(View.VISIBLE);
                    stepDetailCard.setVisibility(View.VISIBLE);
                    exoFullscreen = FULLSCREEN_OFF;

                }

            }

        }

    }

    /**
     * This method starts ExoPlayer.
     *
     * @param context Some Context is anytime useful.
     */
    private void initializeExoPlayer(Context context) {

        if (simpleExoPlayer == null) {

            dummyView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context,new DefaultTrackSelector(),
                    new DefaultLoadControl());

            simpleExoPlayer.addListener(new BakingPlayerEventListener());

            playerView.setPlayer(simpleExoPlayer);

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(playedLink),
                    new DefaultDataSourceFactory(context, Util.getUserAgent(context,getString(R.string.app_name))),
                    new DefaultExtractorsFactory(),
                    null,
                    null);

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.seekTo(playerPosition);
            simpleExoPlayer.setPlayWhenReady(playerWhenReady);

        }

    }

    /**
     * Almost generic event listener for ExoPlayer.
     */
    class BakingPlayerEventListener implements Player.EventListener {

        private boolean failed = false;

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            if (playbackState == Player.STATE_ENDED) {

                simpleExoPlayer.seekTo(0);
                simpleExoPlayer.setPlayWhenReady(false);

            }

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

            failed = true;

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    }

}
