package com.joe.epmediademo.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.joe.epmediademo.R;
import com.joe.epmediademo.Utils.ScreenTools;

import java.io.File;

public class ExoplayerConcatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";

    private ComponentListenerSecond componentListenerSecond;
    private SimpleExoPlayer exoPlayerSecond;
    ConcatenatingMediaSource concatenatedSource;
    MediaSource videoSourceSingle;
    private ControlDispatcher controlDispatcher;

    View iv_play;
    Button btn_chang_mediasource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer_concat);

        iv_play = findViewById(R.id.iv_play);
        PlayerView playerView = findViewById(R.id.video_player);
        playerView.setOnClickListener(this);

        int screenWidth = ScreenTools.getScreenWidth();
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
        params.height = screenWidth * 9 / 16;
        params.width = screenWidth;

        String video = Environment.getExternalStorageDirectory()+"/xijing/video/aae6a336-8280-4295-afed-03d66d142dbb.mp4";

        setPlayer(playerView, video);

        btn_chang_mediasource = findViewById(R.id.btn_chang_mediasource);
        btn_chang_mediasource.setOnClickListener(this);
    }

    private void setPlayer(PlayerView playerView, String video) {
        //https://www.cnblogs.com/renhui/p/10822692.html
        controlDispatcher = new DefaultControlDispatcher();
        exoPlayerSecond = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
//        exoPlayerSecond.setVolume(0);
        componentListenerSecond = new ComponentListenerSecond();
        exoPlayerSecond.addListener(componentListenerSecond);
//        exoPlayerSecond.setSeekParameters(SeekParameters.CLOSEST_SYNC);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Multimedia"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(new File(video)));
        // 从第5s开始剪辑到第10s
        ClippingMediaSource clippingSource = new ClippingMediaSource(videoSource,
                /* startPositionUs= */ 3_000_000, /* endPositionUs= */ 15_000_000);

        MediaSource source = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(new File(video)));
        // Plays the video twice.
//        LoopingMediaSource loopingSource = new LoopingMediaSource(source, 2);
        concatenatedSource = new ConcatenatingMediaSource(clippingSource, source);
        videoSourceSingle = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.fromFile(new File(video)));

        playerView.setUseController(false);
        playerView.setPlayer(exoPlayerSecond);
        exoPlayerSecond.prepare(concatenatedSource);
        //在切换媒体源时我只使用 player.stop(); player.prepare(mediaSource);将导致大约0.5秒的黑屏，直到它切换视频
        playerView.setShutterBackgroundColor(Color.TRANSPARENT);
    }

    boolean needReset;
    @Override
    protected void onResume() {
        super.onResume();
        if(exoPlayerSecond.getPlaybackState() == Player.STATE_ENDED)
            needReset = true;
//        if(status ==0){
//            exoPlayerSecond.prepare(concatenatedSource, false, true);
//        } else {
//            exoPlayerSecond.prepare(videoSourceSingle, false, true);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        iv_play.setVisibility(View.VISIBLE);
        exoPlayerSecond.setPlayWhenReady(false);
    }

    @Override
    protected void onDestroy() {
        exoPlayerSecond.removeListener(componentListenerSecond);
        exoPlayerSecond.release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_player:
                if(isPlaying(exoPlayerSecond)){
                    iv_play.setVisibility(View.VISIBLE);
                    exoPlayerSecond.setPlayWhenReady(false);
                } else {
                    iv_play.setVisibility(View.GONE);
                    exoPlayerSecond.setPlayWhenReady(true);
                }
                break;
            case R.id.btn_chang_mediasource:
                if(status == 0){
                    status = 1;
                    btn_chang_mediasource.setText("修改数据源 (now单)");
                    exoPlayerSecond.stop(true);
                    exoPlayerSecond.prepare(videoSourceSingle, true, true);
                } else {
                    status = 0;
                    btn_chang_mediasource.setText("修改数据源 (now循环)");
                    exoPlayerSecond.stop(true);
                    exoPlayerSecond.prepare(concatenatedSource, true, true);
                }
                break;
        }
    }
    int status;

    private boolean isPlaying(SimpleExoPlayer exoPlayer) {
        return exoPlayer != null
                && exoPlayer.getPlaybackState() != Player.STATE_ENDED
                && exoPlayer.getPlaybackState() != Player.STATE_IDLE
                && exoPlayer.getPlayWhenReady();
    }

    long duration;
    private final class ComponentListenerSecond implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d(TAG,"Second onPlayerStateChanged  playWhenReady="+playWhenReady+"    playbackState="+playbackState);
            switch (playbackState){
                case Player.STATE_READY:
                    duration = exoPlayerSecond.getDuration();
                    Log.d(TAG, "STATE_READY  duration="+duration);
                    break;
                case Player.STATE_ENDED:
                    exoPlayerSecond.seekTo(0, 3000);
                    if(needReset) {
                        needReset = false;
                        Log.d(TAG, "CurrentPosition duration - 100   WindowIndex= "+exoPlayerSecond.getCurrentWindowIndex());
//                        exoPlayerSecond.seekTo(exoPlayerSecond.getCurrentWindowIndex(), duration - 100);
//                        exoPlayerSecond.seekTo(exoPlayerSecond.getCurrentWindowIndex(), C.TIME_UNSET);
//                        controlDispatcher.dispatchSeekTo(exoPlayerSecond, exoPlayerSecond.getCurrentWindowIndex(), C.TIME_END_OF_SOURCE);
                    } else {
                        Log.d(TAG, "CurrentPosition = "+exoPlayerSecond.getCurrentPosition()+"  duration="+duration
                                +"    CurrentPeriodIndex="+exoPlayerSecond.getCurrentPeriodIndex());
                    }
                    break;
            }
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            //在MediaSource更改时被调用（此时我可以听到新的曲目），因此它看起来是正确的位置，但如何检索正在播放的MediaSource的索引
            // Can I retrieve the index of the MediaSource currently being played here?
            // If not, where!?
            Log.d(TAG, "Second onTracksChanged  "+trackSelections.length);
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            //获取当前正在播放的MediaSource的索引的方法
            int sourceIndex = exoPlayerSecond.getCurrentWindowIndex();
            Log.d(TAG, "Second onPositionDiscontinuity  sourceIndex="+sourceIndex
                    +"     reason="+reason+"    CurrentPeriodIndex="+exoPlayerSecond.getCurrentPeriodIndex());
        }
    }
}
