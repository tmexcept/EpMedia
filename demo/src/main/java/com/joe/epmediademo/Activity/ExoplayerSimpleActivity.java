package com.joe.epmediademo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.joe.epmediademo.R;
import com.joe.epmediademo.Utils.ScreenTools;

import java.io.File;

public class ExoplayerSimpleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";

    private ControlDispatcher controlDispatcherV;
    private ComponentListenerV componentListenerV;
    private SimpleExoPlayer exoPlayerV;
    private MediaSource videoSource;

    View iv_play;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer_simple);

        iv_play = findViewById(R.id.iv_play);
        PlayerView playerView = findViewById(R.id.video_player);
        playerView.setOnClickListener(this);
        findViewById(R.id.btn_open).setOnClickListener(this);

        int screenWidth = ScreenTools.getScreenWidth();
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
        params.height = screenWidth * 9 / 16;
        params.width = screenWidth;

        exoPlayerV = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        exoPlayerV.setVolume(0);
        componentListenerV = new ComponentListenerV();
        exoPlayerV.addListener(componentListenerV);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Multimedia"));
        videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.fromFile(
                        new File(Environment.getExternalStorageDirectory()+"/xijing/video/0def6a4e-4911-4269-bda9-02babb2e4e7b.mp4")
                ));
        playerView.setUseController(false);
        playerView.setPlayer(exoPlayerV);
        exoPlayerV.prepare(videoSource);
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_play.setVisibility(View.GONE);
        exoPlayerV.prepare(videoSource, false, true);
        exoPlayerV.setPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        iv_play.setVisibility(View.VISIBLE);
        exoPlayerV.setPlayWhenReady(false);
    }

    @Override
    protected void onDestroy() {
        exoPlayerV.removeListener(componentListenerV);
        exoPlayerV.release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_player:
                if(isPlaying(exoPlayerV)){
                    iv_play.setVisibility(View.VISIBLE);
                    exoPlayerV.setPlayWhenReady(false);
                } else {
                    iv_play.setVisibility(View.GONE);
                    exoPlayerV.setPlayWhenReady(true);
                }
                break;
            case R.id.btn_open:
                startActivity(new Intent(this, ExoplayerSimpleActivity.class));
            break;
        }
    }

    private boolean isPlaying(SimpleExoPlayer exoPlayer) {
        return exoPlayer != null
                && exoPlayer.getPlaybackState() != Player.STATE_ENDED
                && exoPlayer.getPlaybackState() != Player.STATE_IDLE
                && exoPlayer.getPlayWhenReady();
    }

    private final class ComponentListenerV implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d(TAG,"onPlayerStateChanged  playWhenReady="+playWhenReady+"    playbackState="+playbackState);
            switch (playbackState){
                case Player.STATE_READY:
                    break;
                case Player.STATE_ENDED:

                    break;
            }
        }

        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
            Log.d(TAG,"onTimelineChanged  =");

            int position = (int) exoPlayerV.getCurrentPosition();
        }
    }
}
